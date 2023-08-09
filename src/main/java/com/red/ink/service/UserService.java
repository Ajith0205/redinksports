/**
 * 
 */
package com.red.ink.service;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.red.ink.dto.ChangePassword;
import com.red.ink.dto.ForgotPassword;
import com.red.ink.mobileOTP.PasswordResetRequestDto;
import com.red.ink.model.ForgotpasswordOTP;
import com.red.ink.model.User;

/**
 * @author bsoft-ajit
 *
 */
public interface UserService {

	
	/*
	 * Optional<User> findByUsernameAndPassword(String username, String password);
	 * 
	 * Optional<User> findByUsername(String username);
	 * 
	 * List<User> findAll();
	 */

public	ResponseEntity<Object> save(User user);

public ResponseEntity<Object> changePasswordByUser(ChangePassword changePassword, String token);

public ResponseEntity<Object> forgotPasswordByUser(ForgotPassword forgetPassword);

//public ResponseEntity<Object> findUserById(Long id);

//public ResponseEntity<Object> deleteUserById(Long id);

public ResponseEntity<Object> forgotPasswordByUserGetOTP(ForgotPassword forgotPassword);

public ResponseEntity<Object> otpCreatePassword(ChangePassword changePassword);

public ResponseEntity<Object> save(MultipartFile[] file, String token);

public List<User> findAll();

public ResponseEntity<Object> findUserById(Long id, String token);

public ResponseEntity<Object> deleteUserById(Long id, String token);

public ResponseEntity<Object> findAll(String token);

public ResponseEntity<Object> updateUser(User user, String token);

public ResponseEntity<Object> otpSend(PasswordResetRequestDto otpRequest);

public ResponseEntity<Object> createMobilePasswordOTP(ChangePassword changePassword);



//public ResponseEntity<Object> findUserById(Long id);






	



}
