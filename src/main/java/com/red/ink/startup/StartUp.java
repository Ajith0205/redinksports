package com.red.ink.startup;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.red.ink.configuration.BcryptPasswordEncoder;
import com.red.ink.model.Role;
import com.red.ink.model.User;
import com.red.ink.repository.RoleRepository;
import com.red.ink.repository.UserRepository;



@Service
public class StartUp  implements CommandLineRunner{
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		createRoleIfNotExist("ADMIN");
		createRoleIfNotExist("Trainer");
		createRoleIfNotExist("Player");

		createAdminUserIfNotExist("admin");
		
	}

	private void createAdminUserIfNotExist(String username) {
		Optional<User> checkUser = userRepository.findByUsername(username);
		if (!checkUser.isPresent()) {
			User user = new User(username, bcryptPasswordEncoder.encode("ajith"), username,"male","1234567890", "ajith02051997@gmail.com",
					  "Chennai", true,
					roleRepository.findByRole("ADMIN").get());
			user.setPermissions("ADD,EDIT,DELETE,LIST");
			userRepository.saveAndFlush(user);
		}
	}

	private void createRoleIfNotExist(String role) {
		Optional<Role> checkUserRole = roleRepository.findByRole(role);
		if (!checkUserRole.isPresent()) {
			Role role2 = new Role(role, role, true);
			roleRepository.saveAndFlush(role2);
		}
	}

}
