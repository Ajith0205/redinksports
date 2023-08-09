/**
 * 
 */
package com.red.ink.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.red.ink.repository.ForgotpasswordMobileOTPRepository;
import com.red.ink.service.ForgotpasswordMobileOTPService;

/**
 * @author Ajith
 *
 */
public class ForgotpasswordMobileOTPServiceImpl implements ForgotpasswordMobileOTPService {

	@Autowired
	ForgotpasswordMobileOTPRepository forgotpasswordMobileOTPRepository;
}
