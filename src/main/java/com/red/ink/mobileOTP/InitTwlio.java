/**
 * 
 */
package com.red.ink.mobileOTP;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.twilio.Twilio;

/**
 * @author ajith
 *
 */
public class InitTwlio {
	
	@Autowired
	TwilioConfig configu;
	
	@PostConstruct
	public void initTwilio(){
		Twilio.init(configu.getAccountSid(),configu.getAuthToken());
	}

}
