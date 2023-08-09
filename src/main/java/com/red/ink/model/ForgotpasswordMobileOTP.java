/**
 * 
 */
package com.red.ink.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Ajith
 *
 */
@Entity
@Table(name="tbl_ForgetPasswordMobileOTP")
public class ForgotpasswordMobileOTP {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String otp;
	private Date OtpVerificationTime;
	
	@OneToOne
	private User user;
//	@Transient
//	private User userId;
	
	public ForgotpasswordMobileOTP() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	public ForgotpasswordMobileOTP(String otp, Date otpVerificationTime, User user) {
		super();
		this.otp = otp;
		OtpVerificationTime = otpVerificationTime;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getOtpVerificationTime() {
		return OtpVerificationTime;
	}


	public void setOtpVerificationTime(Date otpVerificationTime) {
		OtpVerificationTime = otpVerificationTime;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ForgotpasswordOTP [id=" + id + ", otp=" + otp + ", OtpVerificationTime=" + OtpVerificationTime
				+ ", user=" + user + "]";
	}

}
