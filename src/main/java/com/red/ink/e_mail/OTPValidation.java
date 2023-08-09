/**
 * 
 */
package com.red.ink.e_mail;

import org.springframework.stereotype.Component;

import com.red.ink.dto.ChangePassword;
import com.red.ink.model.ForgotpasswordMobileOTP;
import com.red.ink.model.ForgotpasswordOTP;

/**
 * @author ajith
 *
 */
@Component
public class OTPValidation {
	
	
//	ForgotpasswordOTP forgotOtp;
	
	private static final long OTP_VALID_DURATION = 5 * 60 * 1000;//five Minutes
	public boolean isOTPRequired( ForgotpasswordOTP forgotpasswordOTP) {
        if (forgotpasswordOTP.getOtp() == null) {
            return false;
        }
         
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis=forgotpasswordOTP.getOtpVerificationTime().getTime();
         
        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
            // OTP expires
            return false;
        }
         
        return true;
    }
	public boolean isMobileOTPRequired(ForgotpasswordMobileOTP forgotpasswordOTP) {
		if (forgotpasswordOTP.getOtp() == null) {
            return false;
        }
         
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis=forgotpasswordOTP.getOtpVerificationTime().getTime();
         
        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
            // OTP expires
            return false;
        }
         
        return true;
	}
	
//	public boolean isOTPRequired(String otp) {
//		if (forgotOtp.getOtp() == null) {
//            return false;
//        }
//         
//        long currentTimeInMillis = System.currentTimeMillis();
//        long otpRequestedTimeInMillis=forgotOtp.getOtpVerificationTime().getTime();
//         
//        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
//            // OTP expires
//            return false;
//        }
//         
//        return true;
//	}

}
