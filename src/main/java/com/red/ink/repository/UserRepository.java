/**
 * 
 */
package com.red.ink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.ink.model.ForgotpasswordOTP;
import com.red.ink.model.User;

/**
 * @author bsoft-ajit
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsernameAndPassword(String username, String password);

	Optional<User> findByUsername(String username);

	Optional<User> findByWhatsappNo(String phoneNumber);

	



	


	

	

}
