/**
 * 
 */
package com.red.ink.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.red.ink.configuration.BcryptPasswordEncoder;
import com.red.ink.constant.Constants;
import com.red.ink.dto.ChangePassword;
import com.red.ink.dto.ForgotPassword;
import com.red.ink.e_mail.OTPValidation;
import com.red.ink.e_mail.RandomPasswordGenerator;
import com.red.ink.e_mail.SendMail;
import com.red.ink.jsonconstractor.JsonConstractor;
import com.red.ink.mobileOTP.PasswordResetRequestDto;
import com.red.ink.mobileOTP.PasswordResetResponseDto;
import com.red.ink.mobileOTP.TwilioOTPService;
import com.red.ink.model.ForgotpasswordMobileOTP;
import com.red.ink.model.ForgotpasswordOTP;
import com.red.ink.model.Role;
import com.red.ink.model.User;
import com.red.ink.repository.ForgotpasswordMobileOTPRepository;
import com.red.ink.repository.ForgotpasswordOTPRepository;
import com.red.ink.repository.UserRepository;
import com.red.ink.service.RoleService;
import com.red.ink.service.UserService;
import com.twilio.http.Request;

/**
 * @author bsoft-ajith
 *
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    TwilioOTPService service;
    
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ForgotpasswordOTPRepository forgotpasswordOTPRepository;
	
	@Autowired
	OTPValidation otpValidation;
	@Autowired
	private RandomPasswordGenerator randomPasswordGenerator;

	@Autowired
	private SendMail sendMail;
	@Autowired
	private BcryptPasswordEncoder bcryptPasswordEncoder;
	@Autowired
	private ForgotpasswordMobileOTPRepository forgotpasswordMobileOTPRepository;

	JsonConstractor jsonConstactor = new JsonConstractor();

	@Override
	public ResponseEntity<Object> save(User user) {

		Optional<User> users = userRepository.findByUsername(user.getUsername());

		if ((users.isEmpty() && (!user.getUsername().isEmpty()))) {
			Long roleId = (long) 0;
			if (user.getSelectRole().equals("Trainer")) {
				roleId = (long) 2;
			} else if (user.getSelectRole().equals("Player")) {
				roleId = (long) 3;
			}

			if (roleId != 0) {

				Role role = roleService.getById(roleId);
				user.setRole(role);

				User user1 = new User();
				user1.setPermissions("ADD,EDIT,DELETE,LIST");
				user1.setName(user.getName());
				user1.setAadharNo(user.getAadharNo());
				user1.setAddress(user.getAddress());
				user1.setDateofbirth(user.getDateofbirth());
				user1.setEmail(user.getEmail());
				user1.setFatherName(user.getFatherName());
				user1.setGender(user.getGender());
				user1.setPanNo(user.getPanNo());
				user1.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
				user1.setPhysicalStatus(user.getPhysicalStatus());
				user1.setPlaceofBirth(user.getPlaceofBirth());
				user1.setProfile(user.getProfile());
				user1.setRole(user.getRole());
				user1.setRoleId(roleId);
				user1.setSelectgame(user.getSelectgame());
				user1.setSelectRole(user.getSelectRole());
				user1.setStatus(true);
				user1.setUploadAadhar(user.getUploadAadhar());
				user1.setUploadPAN(user.getUploadPAN());
				user1.setWhatsappNo(user.getWhatsappNo());
				user1.setUsername(user.getUsername());

				// profile Upload
				if (user.getProfile() != null && user.getProfile() != "") {
					String imageUser = user.getProfile();

					if (user.getProfile() != null && user.getProfile() != "") {
						String fileName = user.getProfile().substring(user.getProfile().lastIndexOf("/"));
						String filePath = Constants.USERPROFILEIMG + fileName;
					File dest = new File(filePath);
						//File dest = new File("/var/www/html/userProfileImages/A.png");
						
						dest.delete();
//						System.out.println("status"+dest.exists());

					}
					String path = null;
					boolean base64 = false;
					// photo path can send data start that only for Base64 datas
					if (user.getProfile().startsWith("data:")) {
						String[] arrOfStr = user.getProfile().split(",");
						String[] arrOfStr1 = arrOfStr[0].split(";");
						if (arrOfStr1[1].equalsIgnoreCase("base64")) {
							base64 = true;
						} else {
							base64 = false;
						}
					}
					if (base64) {

						String[] path1 = user.getProfile().split("/");
						String fileName = path1[path1.length - 1];
						Path myPath = Paths.get(Constants.USERPROFILEIMG + "/" + fileName);
						try {
							Files.delete(myPath);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						String[] parts = user.getProfile().split(",");
						String imageString = parts[1];
						try {
							File dir = new File(Constants.USERPROFILEIMG);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							BufferedImage image = null;
							Decoder decoder = Base64.getDecoder();
							byte[] resultImage = decoder.decode(imageString);
							ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
							image = ImageIO.read(bais);
							bais.close();

							String fileName1 = user1.getUsername() + "Profile"+"." + "png";
							String filePath = Constants.USERPROFILEIMG + fileName1;
							File outputFile = new File(filePath);
							ImageIO.write(image, "png", outputFile);
							path = Constants.USERPROFILEIMG_ACCESSPATH + fileName1;
							user1.setProfile(path);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

				// aadharUpload

				if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
					String imageUser = user.getUploadAadhar();

					if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
						String fileName = user.getUploadAadhar().substring(user.getUploadAadhar().lastIndexOf("/"));
						String filePath = Constants.USERAADHARIMG + fileName;
						File dest = new File(filePath);
						dest.delete();

					}
					String path = null;
					boolean base64 = false;
					if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
						// photo path can send data start that only for Base64 datas
						if (user.getUploadAadhar().startsWith("data:")) {
							String[] arrOfStr = user.getUploadAadhar().split(",");
							String[] arrOfStr1 = arrOfStr[0].split(";");
							if (arrOfStr1[1].equalsIgnoreCase("base64")) {
								base64 = true;
							} else {
								base64 = false;
							}

						}
						if (base64) {
							if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
								String[] path1 = user.getUploadAadhar().split("/");
								String fileName = path1[path1.length - 1];
								Path myPath = Paths.get(Constants.USERAADHARIMG + "/" + fileName);

								try {
									Files.delete(myPath);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							String[] parts = user.getUploadAadhar().split(",");
							String imageString = parts[1];
							try {
								File dir = new File(Constants.USERAADHARIMG);
								if (!dir.exists()) {
									dir.mkdirs();
								}
								BufferedImage image = null;
								Decoder decoder = Base64.getDecoder();
								byte[] resultImage = decoder.decode(imageString);
								ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
								image = ImageIO.read(bais);
								bais.close();

								String fileName =user1.getUsername() +"Aadhar"+"." + "png";
								String filePath = Constants.USERAADHARIMG + fileName;
								File outputFile = new File(filePath);
								ImageIO.write(image, "png", outputFile);
								path = Constants.USERAADHARIMG_ACCESSPATH + fileName;
								user1.setUploadAadhar(path);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}

				// pan upload

				if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
					String imageUser = user.getUploadPAN();

					if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
						String fileName = user.getUploadPAN().substring(user.getUploadPAN().lastIndexOf("/"));
						String filePath = Constants.USERPANIMG + fileName;
						File dest = new File(filePath);
						dest.delete();
					}
					String path = null;
					boolean base64 = false;
					if (user.getUploadPAN() != null && user.getUploadPAN() != "") {

						// photo path can send data start that only for Base64 datas
						if (user.getUploadPAN().startsWith("data:")) {
							String[] arrOfStr = user.getUploadPAN().split(",");
							String[] arrOfStr1 = arrOfStr[0].split(";");
							if (arrOfStr1[1].equalsIgnoreCase("base64")) {
								base64 = true;
							} else {
								base64 = false;
							}

						}
						if (base64) {
							if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
								String[] path1 = user.getUploadPAN().split("/");
								String fileName = path1[path1.length - 1];
								Path myPath = Paths.get(Constants.USERPANIMG + "/" + fileName);

								try {
									Files.delete(myPath);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							String[] parts = user.getUploadPAN().split(",");
							String imageString = parts[1];
							try {
								File dir = new File(Constants.USERPANIMG);
								if (!dir.exists()) {
									dir.mkdirs();
								}
								BufferedImage image = null;
								Decoder decoder = Base64.getDecoder();
								byte[] resultImage = decoder.decode(imageString);
								ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
								image = ImageIO.read(bais);
								bais.close();
								String fileName = user1.getUsername() + "PAN"+"." + "png";
								String filePath = Constants.USERPANIMG + fileName;
								File outputFile = new File(filePath);
								ImageIO.write(image, "png", outputFile);
								path = Constants.USERPANIMG_ACCESSPATH + fileName;
								user1.setUploadPAN(path);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}

				User user2 = userRepository.save(user1);

				return jsonConstactor.responseCreation(true, "User Saved Sucess", null, null,null);
			}

		}

		return jsonConstactor.responseCreation(false, "UserName is Already Exists", null, null, null);
	}

	@Override
	public ResponseEntity<Object> changePasswordByUser(ChangePassword changePassword, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (changePassword != null) {
				if (loggedInUser.isPresent()) {

					boolean passwordMatches = BcryptPasswordEncoder.checkpw(changePassword.getOldPassword(),
							loggedInUser.get().getPassword());
					if (passwordMatches) {
						if (changePassword.getNewPassword() != null && changePassword.getConfirmPassword() != null) {
							if (changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
								loggedInUser.get()
										.setPassword(bcryptPasswordEncoder.encode(changePassword.getConfirmPassword()));
								//User user = new User();
								
								userRepository.save(loggedInUser.get());
								return jsonConstactor.responseCreation(true, "change the password", "success", null,
										null);
							} else {
								return jsonConstactor.responseCreation(false, "The Password is", "Miss Matched", null,
										null);
							}
						} else {
							return jsonConstactor.responseCreation(false, "The Password value is Canot be Empty", null,
									null, null);

						}
					} else {
						return jsonConstactor.responseCreation(false, "Password canot match", null, null, null);
					}
				} else {
					return jsonConstactor.responseCreation(false, "User is Not Valid", null, null, null);
				}
			} else {
				return jsonConstactor.responseCreation(false, "can't be null", null, null, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ResponseEntity<Object> forgotPasswordByUser(ForgotPassword forgetPassword) {
		if (forgetPassword != null) {
//			if (forgetPassword.getUsername() != null && forgetPassword.getUsername() != "") {
			Optional<User> loggedInUser = userRepository.findByUsername(forgetPassword.getUsername());
			if (forgetPassword.getUsername() != null && forgetPassword.getUsername() != "") {
				if (forgetPassword.getUsername().equals(loggedInUser.get().getUsername())) {
					if (forgetPassword.getEmail().equals(loggedInUser.get().getEmail())) {
						if (loggedInUser.isPresent()) {
							String password = randomPasswordGenerator.generateRandomString();
							if (forgetPassword.getEmail() != null && forgetPassword.getEmail() != "") {

								// emaild Id Validation
								if (forgetPassword.getEmail().equals(loggedInUser.get().getEmail())) {
									String emailLoginContent = "Website: " + "<a href = " + Constants.URL
											+ ">www.redinksports.com</a><br/>";
									String[] receivers = new String[] { forgetPassword.getEmail() };
									SendMail.sendMail(receivers, "Hi " + loggedInUser.get().getName() + ","
											+ "<br /><br />"
											+ "Your REDINK forgot password is successful done. Please find your login details below."
											+ "<br />" + emailLoginContent + "Username: "
											+ loggedInUser.get().getUsername() + "<br />" 
											+ "Password: " + password
											+ "<br /><br />" + "Thanks & Regards," + "<br />" + "REDINK Sports Team",
											"REDINK Reset Password", null);
                                   

									userRepository.save(loggedInUser.get());

									return jsonConstactor.responseCreation(true, "password can be send your mail", null, null,
											null);
								} else {
									return jsonConstactor.responseCreation(false, "Email Id Cannot Matched",
											null, null, null);
								}
							} else {
								return jsonConstactor.responseCreation(false, "Failed", "Unauthorized user!", null,
										null);
							}
						} else {
							return jsonConstactor.responseCreation(false, "Failed", "User can't be null!!!", null,
									null);
						}
					}  else {
						return jsonConstactor.responseCreation(false, "Failed", "emaild id can't be Matched!!!", null, null);
					}
					
					
					
				} else {
					return jsonConstactor.responseCreation(false, "Failed", "Username can't be Matched!!!", null, null);
				}

			}
			//

		} else {
			return jsonConstactor.responseCreation(false, "Failed", "Forgot Password Information can't be empty!!!",
					null, null);
		}
		return null;
	}


	@Override
	public ResponseEntity<Object> forgotPasswordByUserGetOTP(ForgotPassword forgotPassword) {
		if(forgotPassword != null) {
			Optional<User> loggedInUser = userRepository.findByUsername(forgotPassword.getUsername());
			
			if (forgotPassword.getUsername() != null && forgotPassword.getUsername() != "") {
				if (forgotPassword.getUsername().equals(loggedInUser.get().getUsername())) {
					if (loggedInUser != null &&loggedInUser.isPresent() ) {
						//OTPValidation otpValidation=new OTPValidation();
//						ForgotpasswordOTP forgot = new ForgotpasswordOTP();
						
						ForgotpasswordOTP forgotpasswordOTP = forgotpasswordOTPRepository.findByUser(loggedInUser.get());
						
						String otp = randomPasswordGenerator.generateOTP();
						//	String encodedOTP = bcryptPasswordEncoder.encode(otp);
							
							
							//ForgotpasswordOTP forgotOTP1=otpValidation.isOTPRequired(forgotOTP.getOtp());
						if(forgotpasswordOTP == null) {
							ForgotpasswordOTP forgot = new ForgotpasswordOTP(otp,new Date(), loggedInUser.get());
//							forgot.setUser(loggedInUser.get());
//							forgot.setOtp(otp);
//							forgot.setOtpVerificationTime(new Date());
							forgotpasswordOTPRepository.save(forgot);

						}else {
							forgotpasswordOTP.setOtp(otp);
							forgotpasswordOTP.setOtpVerificationTime(new Date());
							forgotpasswordOTPRepository.saveAndFlush(forgotpasswordOTP);

						}
			
						if (forgotPassword.getEmail().equals(loggedInUser.get().getEmail())) {
							String emailLoginContent = "Website: " + "<a href = " + Constants.URL
									+ ">www.redinksports.com</a><br/>";
							String[] receivers = new String[] { 
									forgotPassword.getEmail()
									};
							SendMail.sendMail(receivers, "Hi " + loggedInUser.get().getName() + ","
									+ "<br /><br />"
									+ "Your REDINK forgotpassword OTP is successful done. Please find your login details below."
									+ "<br />" + emailLoginContent + "Username: "
									+ loggedInUser.get().getUsername() + "<br />" 
									+ "OTP: " + otp
									+ "<br /><br />" + "Thanks & Regards," + "<br />" + "REDINK Sports Club",
									"REDINK ForgotPassword OTP", null);
						//	loggedInUser.get().setOtp(otp);
							return jsonConstactor.responseCreation(true, "OTP can be send your mail", null, null,
									null);
					}
						
						
						
				}
				
			}
			
			
		}

		
		
		return null;
	}
		return null;

}
	
	
	
	public void clearOTP(ForgotpasswordOTP forgetPassword) {
		
	Optional<User> loggedInUser = userRepository.findByUsername(forgetPassword.getUser().getUsername());
	forgetPassword.setOtp(forgetPassword.getOtp());
	forgetPassword.setOtpVerificationTime(forgetPassword.getOtpVerificationTime());
	forgotpasswordOTPRepository.save(forgetPassword);

	}

	@Override
	public ResponseEntity<Object> otpCreatePassword(ChangePassword changePassword) {
		if (changePassword != null) {
			Optional<User> user = userRepository.findByUsername(changePassword.getUsername());
			if(user.isPresent()) {
				ForgotpasswordOTP forgotpasswordOTP = forgotpasswordOTPRepository.findByUser(user.get());
				if(forgotpasswordOTP != null) {
			boolean otpmatches=otpValidation.isOTPRequired(forgotpasswordOTP);
			if(otpmatches) {
					if (changePassword.getNewPassword() != null && changePassword.getConfirmPassword() != null) {
						
						//new password set user
						if (changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
							//user.setPassword(bcryptPasswordEncoder.encode(changePassword.getConfirmPassword()));
						    user.get().setPassword(bcryptPasswordEncoder.encode(changePassword.getConfirmPassword()));
				        	userRepository.save(user.get());
				        	return jsonConstactor.responseCreation(true, "change the password", "success", null,
									null);	
						}
						return jsonConstactor.responseCreation(false, "Password Miss match","check the password", null,
								null);
						
					}
					return jsonConstactor.responseCreation(false, "The OTP is Expired","Try Again", null,
							null);	
				}
			return jsonConstactor.responseCreation(false, "The OTP is Expired","Try Again", null,
					null);
				}else {
					return jsonConstactor.responseCreation(false, "No value present",null, null,
							null);	
				}
			}else {
				return jsonConstactor.responseCreation(false, "No value present",null, null,
						null);	
			}
		}
		
		return null;
	}

	
	@Override
	@Async
	public ResponseEntity<Object> save(MultipartFile[] file, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		
		for(MultipartFile files:file) {
			long start=System.currentTimeMillis();
			List<User>users=parseCSVFile(files);
			
		users=userRepository.saveAll(users);
			
		}
		
		
		return jsonConstactor.responseCreation(true, "Users Saved  Sucess", null, null, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<User> parseCSVFile(MultipartFile files){
		//

		
		
		final List<User>users=new ArrayList<>();
		try {
			try (final BufferedReader br = new BufferedReader(new InputStreamReader(files.getInputStream()))) {
//				Optional<User> users1 = userRepository.findByUsername(files.user.getUsername());
				
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user1 = new User();
                    
                    if ((users.isEmpty() && (!user1.getUsername().isEmpty()))) {
    					Long roleId = (long) 0;
    					if (user1.getSelectRole().equals("Trainer")) {
    						roleId = (long) 2;
    					} else if (user1.getSelectRole().equals("Player")) {
    						roleId = (long) 3;
    					}
    					if (roleId != 0) {	
                    user1.setPermissions("ADD,EDIT,DELETE,LIST");
                    user1.setProfile(data[0]);
    				user1.setName(data[1]);
    				user1.setFatherName(data[2]);
    				user1.setGender(data[3]);
    				user1.setEmail(data[4]);
    				user1.setDateofbirth(data[5]);
    				user1.setPlaceofBirth(data[6]);
    				
    			
    				user1.setPhysicalStatus(data[7]);
    				user1.setAddress(data[8]);
    				user1.setSelectgame(data[9]);
    				user1.setSelectRole(data[10]);
    				
    				user1.setAadharNo(data[11]);
    				user1.setUploadAadhar(data[12]);
    				user1.setPanNo(data[13]);
    				user1.setUploadPAN(data[14]);
    				user1.setWhatsappNo(data[15]);
    				
    				user1.setUsername(data[16]);
    		       bcryptPasswordEncoder.encode(user1.getPassword());	
    			   user1.setPassword(data[17]);
    			    user1.setRole(user1.getRole());
    				user1.setRoleId(roleId);
    			
    				user1.setStatus(true);
    				
    			
    				
                    users.add(user1);
              //      userRepository.saveAll(users);
                    
                    return users;
                    
                    }
    				
    					
                }
                    
                    
                }
               
            }
			
		}catch(final IOException e){
			e.printStackTrace();
			
		}
		
		return null;
	}

	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}

	@Override
	public ResponseEntity<Object> findUserById(Long id, String token) {
		
		String jwtToken = token.replaceFirst("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return jsonConstactor.responseCreation(true,"user Details","get Success", null, user.get());
		}
		return jsonConstactor.responseCreation(false,"user Details","not found", null, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> deleteUserById(Long id, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		 userRepository.deleteById(id);
		
		return jsonConstactor.responseCreation(true,"Account Deleted","Success", null, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> findAll(String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
	List<User>users=userRepository.findAll();
	List<User>usersList=new ArrayList<User>();
	for(User user:users) {
		usersList.add(user);
	}
		return jsonConstactor.responseCreation(true,"Get Users List","Success", usersList, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> updateUser(User user, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if ((user.getId()!=null)&& (user.getUsername() !=null)) {
				Long roleId = (long) 0;
				if (user.getSelectRole().equals("Trainer")) {
					roleId = (long) 2;
				} else if (user.getSelectRole().equals("Player")) {
					roleId = (long) 3;
				}

				if (roleId != 0) {

					Role role = roleService.getById(roleId);
					user.setRole(role);
					
					Optional<User>currentUser=userRepository.findById(user.getId());

					User user1 = new User();
					user1.setPermissions("ADD,EDIT,DELETE,LIST");
					user1.setId(user.getId());
					user1.setName(user.getName());
					user1.setAadharNo(user.getAadharNo());
					user1.setAddress(user.getAddress());
					user1.setDateofbirth(user.getDateofbirth());
					user1.setEmail(user.getEmail());
					user1.setFatherName(user.getFatherName());
					user1.setGender(user.getGender());
					user1.setPanNo(user.getPanNo());
					user1.setPassword(user.getPassword());
//					user1.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
					user1.setPhysicalStatus(user.getPhysicalStatus());
					user1.setPlaceofBirth(user.getPlaceofBirth());
//					user1.setProfile(user.getProfile());
					user1.setRole(user.getRole());
					user1.setRoleId(roleId);
					user1.setSelectgame(user.getSelectgame());
					user1.setSelectRole(user.getSelectRole());
					user1.setStatus(true);
//					user1.setUploadAadhar(user.getUploadAadhar());
//					user1.setUploadPAN(user.getUploadPAN());
					user1.setWhatsappNo(user.getWhatsappNo());
					user1.setUsername(user.getUsername());

					// profile Upload
					if(currentUser.get().getProfile().equals(user.getProfile())) {
						user1.setProfile(user.getProfile());
					}else {
						if (user.getProfile() != null && user.getProfile() != "") {
							String imageUser = user.getProfile();

							if (user.getProfile() != null && user.getProfile() != "") {
								String fileName = user.getProfile().substring(user.getProfile().lastIndexOf("/"));
								String filePath = Constants.USERPROFILEIMG + fileName;
							    File dest = new File(filePath);
								//File dest = new File("/var/www/html/userProfileImages/A.png");
								
								dest.delete();
								System.out.println("status"+dest.exists());

							}
							String path = null;
							boolean base64 = false;
							// photo path can send data start that only for Base64 datas
							if (user.getProfile().startsWith("data:")) {
								String[] arrOfStr = user.getProfile().split(",");
								String[] arrOfStr1 = arrOfStr[0].split(";");
								if (arrOfStr1[1].equalsIgnoreCase("base64")) {
									base64 = true;
								} else {
									base64 = false;
								}
							}
							if (base64) {

								String[] path1 = user.getProfile().split("/");
								String fileName = path1[path1.length - 1];
								Path myPath = Paths.get(Constants.USERPROFILEIMG + "/" + fileName);
								try {
									Files.delete(myPath);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								String[] parts = user.getProfile().split(",");
								String imageString = parts[1];
								try {
									File dir = new File(Constants.USERPROFILEIMG);
									if (!dir.exists()) {
										dir.mkdirs();
									}
									BufferedImage image = null;
									Decoder decoder = Base64.getDecoder();
									byte[] resultImage = decoder.decode(imageString);
									ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
									image = ImageIO.read(bais);
									bais.close();

									String fileName1 = user1.getUsername() + "Profile"+"." + "png";
									String filePath = Constants.USERPROFILEIMG + fileName1;
									File outputFile = new File(filePath);
									ImageIO.write(image, "png", outputFile);
									path = Constants.USERPROFILEIMG_ACCESSPATH + fileName1;
									user1.setProfile(path);

								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
					}
					

					// aadharUpload
					
					if(currentUser.get().getUploadAadhar().equals(user.getUploadAadhar())) {
						user1.setUploadAadhar(user.getUploadAadhar());
					}else {
						if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
							String imageUser = user.getUploadAadhar();

							if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
								String fileName = user.getUploadAadhar().substring(user.getUploadAadhar().lastIndexOf("/"));
								String filePath = Constants.USERAADHARIMG + fileName;
								File dest = new File(filePath);
								dest.delete();

							}
							String path = null;
							boolean base64 = false;
							if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
								// photo path can send data start that only for Base64 datas
								if (user.getUploadAadhar().startsWith("data:")) {
									String[] arrOfStr = user.getUploadAadhar().split(",");
									String[] arrOfStr1 = arrOfStr[0].split(";");
									if (arrOfStr1[1].equalsIgnoreCase("base64")) {
										base64 = true;
									} else {
										base64 = false;
									}

								}
								if (base64) {
									if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
										String[] path1 = user.getUploadAadhar().split("/");
										String fileName = path1[path1.length - 1];
										Path myPath = Paths.get(Constants.USERAADHARIMG + "/" + fileName);

										try {
											Files.delete(myPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
									String[] parts = user.getUploadAadhar().split(",");
									String imageString = parts[1];
									try {
										File dir = new File(Constants.USERAADHARIMG);
										if (!dir.exists()) {
											dir.mkdirs();
										}
										BufferedImage image = null;
										Decoder decoder = Base64.getDecoder();
										byte[] resultImage = decoder.decode(imageString);
										ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
										image = ImageIO.read(bais);
										bais.close();

										String fileName =user1.getUsername() +"Aadhar"+"." + "png";
										String filePath = Constants.USERAADHARIMG + fileName;
										File outputFile = new File(filePath);
										ImageIO.write(image, "png", outputFile);
										path = Constants.USERAADHARIMG_ACCESSPATH + fileName;
										user1.setUploadAadhar(path);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

						}
					}

					

					// pan upload
					if(currentUser.get().getUploadPAN().equals(user.getUploadPAN())) {
						user1.setUploadPAN(user.getUploadPAN());
					}else {
						if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
							String imageUser = user.getUploadPAN();

							if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
								String fileName = user.getUploadPAN().substring(user.getUploadPAN().lastIndexOf("/"));
								String filePath = Constants.USERPANIMG + fileName;
								File dest = new File(filePath);
								dest.delete();
							}
							String path = null;
							boolean base64 = false;
							if (user.getUploadPAN() != null && user.getUploadPAN() != "") {

								// photo path can send data start that only for Base64 datas
								if (user.getUploadPAN().startsWith("data:")) {
									String[] arrOfStr = user.getUploadPAN().split(",");
									String[] arrOfStr1 = arrOfStr[0].split(";");
									if (arrOfStr1[1].equalsIgnoreCase("base64")) {
										base64 = true;
									} else {
										base64 = false;
									}

								}
								if (base64) {
									if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
										String[] path1 = user.getUploadPAN().split("/");
										String fileName = path1[path1.length - 1];
										Path myPath = Paths.get(Constants.USERPANIMG + "/" + fileName);

										try {
											Files.delete(myPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
									String[] parts = user.getUploadPAN().split(",");
									String imageString = parts[1];
									try {
										File dir = new File(Constants.USERPANIMG);
										if (!dir.exists()) {
											dir.mkdirs();
										}
										BufferedImage image = null;
										Decoder decoder = Base64.getDecoder();
										byte[] resultImage = decoder.decode(imageString);
										ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
										image = ImageIO.read(bais);
										bais.close();
										String fileName = user1.getUsername() + "PAN"+"." + "png";
										String filePath = Constants.USERPANIMG + fileName;
										File outputFile = new File(filePath);
										ImageIO.write(image, "png", outputFile);
										path = Constants.USERPANIMG_ACCESSPATH + fileName;
										user1.setUploadPAN(path);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

						}
					}

					
					
					User user2 = userRepository.saveAndFlush(user1);

					return jsonConstactor.responseCreation(true, "User Updated Success", null, null, user2);
				}

			}

			return jsonConstactor.responseCreation(false, "Users is un Authorized", null, null, null);
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ResponseEntity<Object> otpSend(PasswordResetRequestDto otpRequest) {
			Optional<User> loggedInUser = userRepository.findByWhatsappNo(otpRequest.getPhoneNumber());
		if(loggedInUser.isPresent()) {
			if(otpRequest !=null) {
			
				if(otpRequest.getUsername()!=null && otpRequest.getUsername()!="") {
					if(loggedInUser.get().getUsername().equals(otpRequest.getUsername())) {
						if(loggedInUser.get().getWhatsappNo().equals(otpRequest.getPhoneNumber())) {
							if(otpRequest.getPhoneNumber()!=null) {
								String otp=sendOTP(otpRequest);
								ForgotpasswordMobileOTP forgotpasswordOTP = forgotpasswordMobileOTPRepository.findByUser(loggedInUser.get());
								if(forgotpasswordOTP == null) {
									ForgotpasswordMobileOTP forgot = new ForgotpasswordMobileOTP(otp,new Date(), loggedInUser.get());
									forgotpasswordMobileOTPRepository.save(forgot);

								}else {
									forgotpasswordOTP.setOtp(otp);
									forgotpasswordOTP.setOtpVerificationTime(new Date());
									forgotpasswordMobileOTPRepository.saveAndFlush(forgotpasswordOTP);

								}
								
								return jsonConstactor.responseCreation(true, "OTP can be send your Number", null, null,
										null);
								
							}
						}
						return jsonConstactor.responseCreation(false, "Number is miss match", null, null, null);
					}
					return jsonConstactor.responseCreation(false, "Username is miss match", null, null, null);
				}
				return jsonConstactor.responseCreation(false, "Username is null", null, null, null);
				
			}
		}
	
		
		
		return jsonConstactor.responseCreation(false, "Users is un Authorized", null, null, null);
	}

	private String sendOTP(PasswordResetRequestDto otpRequest) {
		PasswordResetResponseDto dto=new PasswordResetResponseDto();
//		 service.sendOTPForPasswordReset(otpRequest.getPhoneNumber(), otp);
	    dto= service.sendOTPForPasswordReset(otpRequest);
	    
	    String otp=dto.getMessage();
		 ServerResponse.status(HttpStatus.OK);
		return otp;
		 
		}

	@Override
	public ResponseEntity<Object> createMobilePasswordOTP(ChangePassword changePassword) {
if (changePassword != null) {
			
			Optional<User> user = userRepository.findByUsername(changePassword.getUsername());
		
			if(user.isPresent()) {
				
				ForgotpasswordMobileOTP forgotpasswordOTP = forgotpasswordMobileOTPRepository.findByUser(user.get());
				if(forgotpasswordOTP != null) {
			boolean otpmatches=otpValidation.isMobileOTPRequired(forgotpasswordOTP);
			
		
			if(otpmatches) {
					if (changePassword.getNewPassword() != null && changePassword.getConfirmPassword() != null) {
						
						//new password set user
						if (changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
						
						    user.get().setPassword(bcryptPasswordEncoder.encode(changePassword.getConfirmPassword()));
				        	userRepository.save(user.get());
				        	return jsonConstactor.responseCreation(true, "change the password", "success", null,
									null);	
						}
						return jsonConstactor.responseCreation(false, "Password Miss match","check the password", null,
								null);
						
					}
					return jsonConstactor.responseCreation(false, "The OTP is Expired","Try Again", null,
							null);	
				}
			return jsonConstactor.responseCreation(false, "The OTP is Expired","Try Again", null,
					null);
				}else {
					return jsonConstactor.responseCreation(false, "No value present",null, null,
							null);	
				}
			}else {
				return jsonConstactor.responseCreation(false, "No value present",null, null,
						null);	
			}
		}
		
		return null;
	}
	
	@Override
	public ResponseEntity<Object> createUser(String token, User user) {
		String jwtToken = token.replaceFirst("Bearer", "");
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if(loggedInUser.isPresent()) {


				Optional<User> users = userRepository.findByUsername(user.getUsername());

				if ((users.isEmpty() && (!user.getUsername().isEmpty()))) {
					Long roleId = (long) 0;
					if (user.getSelectRole().equals("Trainer")) {
						roleId = (long) 2;
					} else if (user.getSelectRole().equals("Player")) {
						roleId = (long) 3;
					}

					if (roleId != 0) {

						Role role = roleService.getById(roleId);
						user.setRole(role);

						User user1 = new User();
						user1.setPermissions("ADD,EDIT,DELETE,LIST");
						user1.setName(user.getName());
						user1.setAadharNo(user.getAadharNo());
						user1.setAddress(user.getAddress());
						user1.setDateofbirth(user.getDateofbirth());
						user1.setEmail(user.getEmail());
						user1.setFatherName(user.getFatherName());
						user1.setGender(user.getGender());
						user1.setPanNo(user.getPanNo());
						String password = randomPasswordGenerator.generateRandomString();
						
						user1.setPassword(bcryptPasswordEncoder.encode(password));
						user1.setPhysicalStatus(user.getPhysicalStatus());
						user1.setPlaceofBirth(user.getPlaceofBirth());
						user1.setProfile(user.getProfile());
						user1.setRole(user.getRole());
						user1.setRoleId(roleId);
						user1.setSelectgame(user.getSelectgame());
						user1.setSelectRole(user.getSelectRole());
						user1.setStatus(true);
						user1.setUploadAadhar(user.getUploadAadhar());
						user1.setUploadPAN(user.getUploadPAN());
						user1.setWhatsappNo(user.getWhatsappNo());
						user1.setUsername(user.getUsername());

						// profile Upload
						if (user.getProfile() != null && user.getProfile() != "") {
							String imageUser = user.getProfile();

							if (user.getProfile() != null && user.getProfile() != "") {
								String fileName = user.getProfile().substring(user.getProfile().lastIndexOf("/"));
								String filePath = Constants.USERPROFILEIMG + fileName;
							File dest = new File(filePath);
								//File dest = new File("/var/www/html/userProfileImages/A.png");
								
								dest.delete();
//								System.out.println("status"+dest.exists());

							}
							String path = null;
							boolean base64 = false;
							// photo path can send data start that only for Base64 datas
							if (user.getProfile().startsWith("data:")) {
								String[] arrOfStr = user.getProfile().split(",");
								String[] arrOfStr1 = arrOfStr[0].split(";");
								if (arrOfStr1[1].equalsIgnoreCase("base64")) {
									base64 = true;
								} else {
									base64 = false;
								}
							}
							if (base64) {

								String[] path1 = user.getProfile().split("/");
								String fileName = path1[path1.length - 1];
								Path myPath = Paths.get(Constants.USERPROFILEIMG + "/" + fileName);
								try {
									Files.delete(myPath);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								String[] parts = user.getProfile().split(",");
								String imageString = parts[1];
								try {
									File dir = new File(Constants.USERPROFILEIMG);
									if (!dir.exists()) {
										dir.mkdirs();
									}
									BufferedImage image = null;
									Decoder decoder = Base64.getDecoder();
									byte[] resultImage = decoder.decode(imageString);
									ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
									image = ImageIO.read(bais);
									bais.close();

									String fileName1 = user1.getUsername() + "Profile"+"." + "png";
									String filePath = Constants.USERPROFILEIMG + fileName1;
									File outputFile = new File(filePath);
									ImageIO.write(image, "png", outputFile);
									path = Constants.USERPROFILEIMG_ACCESSPATH + fileName1;
									user1.setProfile(path);

								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}

						// aadharUpload

						if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
							String imageUser = user.getUploadAadhar();

							if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
								String fileName = user.getUploadAadhar().substring(user.getUploadAadhar().lastIndexOf("/"));
								String filePath = Constants.USERAADHARIMG + fileName;
								File dest = new File(filePath);
								dest.delete();

							}
							String path = null;
							boolean base64 = false;
							if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
								// photo path can send data start that only for Base64 datas
								if (user.getUploadAadhar().startsWith("data:")) {
									String[] arrOfStr = user.getUploadAadhar().split(",");
									String[] arrOfStr1 = arrOfStr[0].split(";");
									if (arrOfStr1[1].equalsIgnoreCase("base64")) {
										base64 = true;
									} else {
										base64 = false;
									}

								}
								if (base64) {
									if (user.getUploadAadhar() != null && user.getUploadAadhar() != "") {
										String[] path1 = user.getUploadAadhar().split("/");
										String fileName = path1[path1.length - 1];
										Path myPath = Paths.get(Constants.USERAADHARIMG + "/" + fileName);

										try {
											Files.delete(myPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
									String[] parts = user.getUploadAadhar().split(",");
									String imageString = parts[1];
									try {
										File dir = new File(Constants.USERAADHARIMG);
										if (!dir.exists()) {
											dir.mkdirs();
										}
										BufferedImage image = null;
										Decoder decoder = Base64.getDecoder();
										byte[] resultImage = decoder.decode(imageString);
										ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
										image = ImageIO.read(bais);
										bais.close();

										String fileName =user1.getUsername() +"Aadhar"+"." + "png";
										String filePath = Constants.USERAADHARIMG + fileName;
										File outputFile = new File(filePath);
										ImageIO.write(image, "png", outputFile);
										path = Constants.USERAADHARIMG_ACCESSPATH + fileName;
										user1.setUploadAadhar(path);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

						}

						// pan upload

						if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
							String imageUser = user.getUploadPAN();

							if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
								String fileName = user.getUploadPAN().substring(user.getUploadPAN().lastIndexOf("/"));
								String filePath = Constants.USERPANIMG + fileName;
								File dest = new File(filePath);
								dest.delete();
							}
							String path = null;
							boolean base64 = false;
							if (user.getUploadPAN() != null && user.getUploadPAN() != "") {

								// photo path can send data start that only for Base64 datas
								if (user.getUploadPAN().startsWith("data:")) {
									String[] arrOfStr = user.getUploadPAN().split(",");
									String[] arrOfStr1 = arrOfStr[0].split(";");
									if (arrOfStr1[1].equalsIgnoreCase("base64")) {
										base64 = true;
									} else {
										base64 = false;
									}

								}
								if (base64) {
									if (user.getUploadPAN() != null && user.getUploadPAN() != "") {
										String[] path1 = user.getUploadPAN().split("/");
										String fileName = path1[path1.length - 1];
										Path myPath = Paths.get(Constants.USERPANIMG + "/" + fileName);

										try {
											Files.delete(myPath);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
									String[] parts = user.getUploadPAN().split(",");
									String imageString = parts[1];
									try {
										File dir = new File(Constants.USERPANIMG);
										if (!dir.exists()) {
											dir.mkdirs();
										}
										BufferedImage image = null;
										Decoder decoder = Base64.getDecoder();
										byte[] resultImage = decoder.decode(imageString);
										ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
										image = ImageIO.read(bais);
										bais.close();
										String fileName = user1.getUsername() + "PAN"+"." + "png";
										String filePath = Constants.USERPANIMG + fileName;
										File outputFile = new File(filePath);
										ImageIO.write(image, "png", outputFile);
										path = Constants.USERPANIMG_ACCESSPATH + fileName;
										user1.setUploadPAN(path);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

						}

						User user2 = userRepository.save(user1);
						
						// emaild Id Validation
						if (user1.getEmail() !=null) {
							String emailLoginContent = "Website: " + "<a href = " + Constants.URL
									+ ">www.redinksports.com</a><br/>";
							String[] receivers = new String[] { user1.getEmail() };
							SendMail.sendMail(receivers, "Hi " + user1.getName() + ","
									+ "<br /><br />"
									+ "Your REDINK  Your Account Create Successfully. Please find your login details below."
									+ "<br />" + emailLoginContent + "Username: "
									+ user1.getUsername() + "<br />" 
									+ "Password: " + password
									+ "<br /><br />" + "Thanks & Regards," + "<br />" + "REDINK Sports ","Team", null);
						} 

						return jsonConstactor.responseCreation(true, "User Saved Sucess", null, null,null);
					}

				}

				return jsonConstactor.responseCreation(false, "UserName is Already Exists", null, null, null);
			
			}
			
			return jsonConstactor.responseCreation(false, "User Token Is not valid",null, null,
					null);	
	}


	
}
