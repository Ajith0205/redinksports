/**
 * 
 */
package com.red.ink.mobileOTP;

/**
 * @author ajith
 *
 */
public class MobileResponseOTP {
	 private boolean status;
	 private String message;
	 
	 public MobileResponseOTP() {
		// TODO Auto-generated constructor stub
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MobileResponseOTP [status=" + status + ", message=" + message + "]";
	}
	 

}
