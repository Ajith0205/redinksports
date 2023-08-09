/**
 * 
 */
package com.red.ink.dto;

/**
 * @author ajith
 *
 */
public class ForgotPassword {
	
	//@NotBlank(message = "enter your mail id")
     private String email;
	///@NotBlank(message = "enter your UserName")
	private String username;
	 
//	private String phoneNo;
//	private String oneTimePassword;
	

	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	

	@Override
	public String toString() {
		return "ForgotPassword [email=" + email + ", username=" + username + ", phoneNo=" +"]";
	}
	
	

}
