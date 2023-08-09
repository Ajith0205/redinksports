/**
 * 
 */
package com.red.ink.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.red.ink.repository.ForgotpasswordOTPRepository;
import com.red.ink.service.ForgotpasswordOTPService;

/**
 * @author ajith
 *
 */
@Service
public class ForgotpasswordOTPServiceImpl implements ForgotpasswordOTPService{

	@Autowired
	private ForgotpasswordOTPRepository forgotpasswordOTPRepository;
}
