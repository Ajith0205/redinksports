/**
 * 
 */
package com.red.ink.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.red.ink.dto.ChangePassword;
import com.red.ink.dto.ForgotPassword;
import com.red.ink.dto.LoginDto;
import com.red.ink.mobileOTP.PasswordResetRequestDto;
import com.red.ink.model.ForgotpasswordOTP;
import com.red.ink.model.User;
import com.red.ink.service.RoleService;
import com.red.ink.service.UserService;

/**
 * @author bsoft-ajit
 *
 */

@RestController
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@PostMapping("save")
	public ResponseEntity<Object> saveUser(@RequestBody User user) {

		return userService.save(user);

	}

	@PostMapping("login")
	public void validateUser(@RequestBody LoginDto user) {
	}
	
	
	/**
	 * Forgot Password  send Otp For Mail
	 * 
	 * Inputs email,username
	 * 
	 * @param forgotPassword
	 * @return
	 */
	
	@PostMapping("forgotpasswordOTP")
	public ResponseEntity<Object> forgotPasswordOTP(@RequestBody ForgotPassword forgotPassword) {
		return userService.forgotPasswordByUserGetOTP(forgotPassword);
	}
	
	


	
	/**
	 * Password Change
	 * Inputs Old Password,New Password ,Conform Password
	 * @param changePassword
	 * @param token
	 * @return
	 */
	@PostMapping("changePassword")
	public ResponseEntity<Object> changePassword(@RequestBody ChangePassword changePassword,
			@RequestHeader("Authorization") String token) {
		return userService.changePasswordByUser(changePassword, token);
	}

	
	/**
	 * Email Otp Validation for 5 minutes
	 * @param changePassword
	 * @return
	 */
	@PostMapping("checkOtp")
	public ResponseEntity<Object> createPasswordOTP(@RequestBody ChangePassword changePassword) {
		return userService.otpCreatePassword(changePassword);
	}
	
	
	
	/**
	 *  password Send to Email 
	 * 
	 * @param forgetPassword
	 * @return
	 */
	@PostMapping("forgotpassword")
	public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPassword forgetPassword) {
		return userService.forgotPasswordByUser(forgetPassword);

	}
	
	/**
	 * get User Details
	 * 
	 * @param id
	 * @param token
	 * @return
	 */

	@GetMapping("userDetails")
	public ResponseEntity<Object> findUserById(@RequestParam("id") Long id,
			@RequestHeader("Authorization") String token) {
		return userService.findUserById(id,token);

	}
	
	/**
	 * delete User
	 * 
	 * @param id
	 * @param token
	 * @return
	 */

	@DeleteMapping("deleteUser")
	public ResponseEntity<Object> deleteUserById(@RequestParam("id") Long id,
			@RequestHeader("Authorization") String token) {
		return userService.deleteUserById(id,token);
	}

	

	/**
	 * 
	 * save Multiple  user
	 * 
	 * @param file
	 * @param token
	 * @return
	 */
	

	@PostMapping("saveMultipleUsers")
	public ResponseEntity<Object> multipleUsersSave(@RequestParam(value = "files") MultipartFile[] file,
			@RequestHeader("Authorization") String token) {
		return userService.save(file, token);

	}
	
	/**
	 * Get All Users
	 * 
	 * @param token
	 * @return
	 */
	
	@GetMapping("getAllUsers")
	public ResponseEntity<Object> getAlluser(@RequestHeader("Authorization") String token){
		return userService.findAll(token);
		
	}
	/**
	 * Update User
	 * 
	 * @param user
	 * @param token
	 * @return
	 */
	@PutMapping("update")
	public ResponseEntity<Object>updateUsers(@RequestBody User user,
		@RequestHeader("Authorization") String token){
				return userService.updateUser(user,token);
		
	}
	
	
	
	/**
	 *  Mobile Otp send to user Mobile 
	 *  
	 * @param otpRequest
	 * @return
	 */
	@PostMapping("mobileotp")
	public ResponseEntity<Object>mobileOTPSend(@RequestBody PasswordResetRequestDto otpRequest){
		return userService.otpSend(otpRequest);
		
	}
	
	/**
	 * 
	 * Mobile Otp Validation for 5 minutes
	 * 
	 * @param changePassword
	 * @return
	 */
	@PostMapping("checkMobileOtp")
	public ResponseEntity<Object> createMobilePasswordOTP(@RequestBody ChangePassword changePassword) {
		return userService.createMobilePasswordOTP(changePassword);
	}

	
	
	/**
	 * user  create Restricted Based
	 * @param token
	 * @param user
	 * @return
	 */
	
	@PostMapping("saveuser")
	public ResponseEntity<Object>createUser(@RequestHeader("Authorization") String token,@RequestBody User user){
		return userService.createUser(token,user);
		
	}


	
	/**
	 * User Login Option enable  and disable
	 * 
	 * @param token
	 * @param id
	 * @param status
	 * @return
	 */
	@GetMapping("statusChange")
	public ResponseEntity<Object>statusChange(@RequestHeader("Authorization") String token,@RequestParam Long id,@RequestParam boolean status){
		return userService.statusChange(token,id,status);
		
	}

	
}
