/**
 * 
 */
package com.red.ink.mobileOTP;

/**
 * @author ajith
 *
 */
public class PasswordResetRequestDto {

    private String phoneNumber;//destination
    private String username;
    private String oneTimePassword;
    
    public PasswordResetRequestDto() {
		// TODO Auto-generated constructor stub
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOneTimePassword() {
		return oneTimePassword;
	}

	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}

	@Override
	public String toString() {
		return "PasswordResetRequestDto [phoneNumber=" + phoneNumber + ", username=" + username + ", oneTimePassword="
				+ oneTimePassword + "]";
	}

	
    
	
}
