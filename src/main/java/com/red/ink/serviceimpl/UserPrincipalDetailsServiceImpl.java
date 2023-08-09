package com.red.ink.serviceimpl;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.red.ink.model.ForgotpasswordOTP;
import com.red.ink.model.User;
import com.red.ink.model.UserPrincipal;
import com.red.ink.repository.UserRepository;


@Service
public class UserPrincipalDetailsServiceImpl  implements UserDetailsService{

	

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	try {
		
		//user details get principal class 
		
		Optional<User>user=userRepository.findByUsername(username);
		
		
		if(user.isPresent()) {
			UserPrincipal userPrincipal=new UserPrincipal(user.get());
			System.out.println(userPrincipal.getAuthorities());
			return userPrincipal;
		}
	}catch(Exception e){
		
	}
	throw new UsernameNotFoundException(username);
	
	}



	

	
}
