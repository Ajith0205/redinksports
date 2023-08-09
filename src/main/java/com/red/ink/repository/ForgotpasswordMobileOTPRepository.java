/**
 * 
 */
package com.red.ink.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.ink.model.ForgotpasswordMobileOTP;
import com.red.ink.model.User;

/**
 * @author Ajith
 *
 */
public interface ForgotpasswordMobileOTPRepository extends JpaRepository<ForgotpasswordMobileOTP, Long> {

	ForgotpasswordMobileOTP findByUser(User user);

}
