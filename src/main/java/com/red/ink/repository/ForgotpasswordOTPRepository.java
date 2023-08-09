/**
 * 
 */
package com.red.ink.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.ink.model.ForgotpasswordOTP;
import com.red.ink.model.User;

/**
 * @author ajith
 *
 */
public interface ForgotpasswordOTPRepository extends JpaRepository<ForgotpasswordOTP, Long> {

	ForgotpasswordOTP findByUser(User user);

//public	Optional<ForgotpasswordOTP> findByOTP(String otp);

}
