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
import javax.persistence.Transient;

/**
 * @author ajith
 *
 */

@Entity
@Table(name="tbl_ForgetPasswordOTP")
public class ForgotpasswordOTP {
	//private static final long OTP_VALID_DURATION = 5 * 60 * 1000;//five Minutes
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String otp;
	private Date OtpVerificationTime;
	
	@OneToOne
	private User user;
//	@Transient
//	private User userId;
	
	public ForgotpasswordOTP() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	public ForgotpasswordOTP(String otp, Date otpVerificationTime, User user) {
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

//
//	public User getUserId() {
//		return userId;
//	}
//
//
//	public void setUserId(User userId) {
//		this.userId = userId;
//	}

	

	


//	public boolean isOTPRequired() {
//        if (this.getOtp() == null) {
//            return false;
//        }
//         
//        long currentTimeInMillis = System.currentTimeMillis();
//        long otpRequestedTimeInMillis = this.OtpVerificationTime.getTime();
//         
//        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
//            // OTP expires
//            return false;
//        }
//         
//        return true;
//    }
     
	


}
