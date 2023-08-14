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
	/*
	 * Forgot Password  send Otp For Mail
	 * @ Inputs email,username
	 * 
	*/
	
	
	@PostMapping("forgotpasswordOTP")
	public ResponseEntity<Object> forgotPasswordOTP(@RequestBody ForgotPassword forgotPassword) {
		return userService.forgotPasswordByUserGetOTP(forgotPassword);
	}
	
	

	/*
	 * Password Change  
	 * @ Inputs Old Password,New Password ,Conform Password
	 * 
	*/
	@PostMapping("changePassword")
	public ResponseEntity<Object> changePassword(@RequestBody ChangePassword changePassword,
			@RequestHeader("Authorization") String token) {
		return userService.changePasswordByUser(changePassword, token);
	}

	/*
	 * Email Otp Validation for 5 minutes
	 * 
	 * 
	*/
	@PostMapping("checkOtp")
	public ResponseEntity<Object> createPasswordOTP(@RequestBody ChangePassword changePassword) {
		return userService.otpCreatePassword(changePassword);
	}
	
	/*
	 * Password To send Email Directly
	 * 
	*/
	@PostMapping("forgotpassword")
	public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPassword forgetPassword) {
		return userService.forgotPasswordByUser(forgetPassword);

	}

	@GetMapping("userDetails")
	public ResponseEntity<Object> findUserById(@RequestParam("id") Long id,
			@RequestHeader("Authorization") String token) {
		return userService.findUserById(id,token);

	}

	@DeleteMapping("deleteUser")
	public ResponseEntity<Object> deleteUserById(@RequestParam("id") Long id,
			@RequestHeader("Authorization") String token) {
		return userService.deleteUserById(id,token);
	}

	

	

	@PostMapping("saveMultipleUsers")
	public ResponseEntity<Object> multipleUsersSave(@RequestParam(value = "files") MultipartFile[] file,
			@RequestHeader("Authorization") String token) {
		return userService.save(file, token);

	}
	
	@GetMapping("getAllUsers")
	public ResponseEntity<Object> getAlluser(@RequestHeader("Authorization") String token){
		return userService.findAll(token);
		
	}
	
	@PutMapping("update")
	public ResponseEntity<Object>updateUsers(@RequestBody User user,
		@RequestHeader("Authorization") String token){
				return userService.updateUser(user,token);
		
	}
	
	/*
	 * Mobile Otp Working Fine
	 *  Otp send 
	*/
	@PostMapping("mobileotp")
	public ResponseEntity<Object>mobileOTPSend(@RequestBody PasswordResetRequestDto otpRequest){
		return userService.otpSend(otpRequest);
		
	}
	
	/*
	 * Mobile Otp Validation for 5 minutes
	 * 
	 * 
	*/
	@PostMapping("checkMobileOtp")
	public ResponseEntity<Object> createMobilePasswordOTP(@RequestBody ChangePassword changePassword) {
		return userService.createMobilePasswordOTP(changePassword);
	}
	
	@PostMapping("saveuser")
	public ResponseEntity<Object>createUser(@RequestHeader("Authorization") String token,@RequestBody User user){
		return userService.createUser(token,user);
		
	}
	

	
}
