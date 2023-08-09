/**
 * 
 */
package com.red.ink.mobileOTP;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ajith
 *
 */

@Configuration
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfig {
    private String accountSid;
    private String authToken;
    private String trialNumber;

    
    public TwilioConfig() {
		// TODO Auto-generated constructor stub
	}


	public String getAccountSid() {
		return accountSid;
	}


	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}


	public String getAuthToken() {
		return authToken;
	}


	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}


	public String getTrialNumber() {
		return trialNumber;
	}


	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}


	@Override
	public String toString() {
		return "TwilioConfig [accountSid=" + accountSid + ", authToken=" + authToken + ", trialNumber=" + trialNumber
				+ "]";
	}
    
	
}
