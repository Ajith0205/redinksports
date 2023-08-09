/**
 * 
 */
package com.red.ink.dto;

import org.hibernate.annotations.DynamicInsert;

/**
 * @author ajith
 *
 */

@DynamicInsert
public class ChangePassword {
	
	
	private String otp;
	private String oldPassword;
	private String username;

	private String newPassword;

	private String confirmPassword;
	
	public ChangePassword() {
		// TODO Auto-generated constructor stub
	}
	
	

	public String getOtp() {
		return otp;
	}



	public void setOtp(String otp) {
		this.otp = otp;
	}



	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	

	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	@Override
	public String toString() {
		return "ChangePassword [otp=" + otp + ", oldPassword=" + oldPassword + ", username=" + username
				+ ", newPassword=" + newPassword + ", confirmPassword=" + confirmPassword + "]";
	}
	
	
	

}
