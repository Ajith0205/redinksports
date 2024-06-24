/**
 * 
 */
package com.red.ink.mobileOTP;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
/**
 * @author ajith
 *
 */
@Service
public class TwilioOTPService {
	 @Autowired
	    private TwilioConfig twilioConfig;

	    Map<String, String> otpMap = new HashMap<>();
	    	    
	    private final String ACCOUNT_SID = "ACbdeb906b7051ce09e6b63e640512aa40";
	    private final String AUTH_TOKEN = "8bc8e38367a226bdc139b9f44be9fbee";
	    private final String TRIAL_PHONE_NUMBER = "+16672708451";
	    
	    /**
	     * Twilio Based Mobole Otp send 
	     * 
	     * @param passwordResetRequestDto
	     * @return
	     */

	   public PasswordResetResponseDto sendOTPForPasswordReset(PasswordResetRequestDto passwordResetRequestDto) {
	    
		   Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	    
	        PasswordResetResponseDto passwordResetResponseDto1 = new PasswordResetResponseDto();
	        try {
	            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
	            
//	            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
	            PhoneNumber from = new PhoneNumber(TRIAL_PHONE_NUMBER);
	            String otp = generateOTP();
	            String otpMessage = "Dear Customer , Your OTP is ##" + otp + "##. Use this Passcode to complete your transaction. Thank You.";

	            
	            // Save the generated OTP in the map
	            otpMap.put(passwordResetRequestDto.getUsername(), otp);
	            
	         // Create the Twilio message and send it
	            Message message = Message.creator(to, from, otpMessage).create();
	            
	          

	            passwordResetResponseDto1 = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
//	            passwordResetResponseDto1 = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
	        } catch (Exception ex) {
	            passwordResetResponseDto1 = new PasswordResetResponseDto(OtpStatus.FAILED, ex.getMessage());
	        }
	        return  passwordResetResponseDto1;
	    
	    
	   }
//
//	    public String validateOTP(String userInputOtp, String userName) {
//	        if (userInputOtp.equals(otpMap.get(userName))) {
//	            otpMap.remove(userName,userInputOtp);
//	            return "Valid OTP please proceed with your transaction !";
//	        } else {
//	            return "Invalid otp please retry !";
//	        }
//	    }

	    //6 digit otp
	    private String generateOTP() {
	        return new DecimalFormat("000000")
	                .format(new Random().nextInt(999999));
	    }

		public void sendOTPForPasswordReset(String phoneNumber, String bodymessage) {
			 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
			  PhoneNumber to = new PhoneNumber(phoneNumber);
	            
//	            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
	            PhoneNumber from = new PhoneNumber(TRIAL_PHONE_NUMBER);
	            Message message = Message.creator(to, from, bodymessage).create();
	         // You can log or process the response from Twilio here
	            System.out.println("SMS SID: " + message.getSid());
	            System.out.println("SMS Status: " + message.getStatus()); 
	            
			
		}
	    


}
