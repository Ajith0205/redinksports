package com.red.ink.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.red.ink.configuration.BcryptPasswordEncoder;
import com.red.ink.constant.Constants;
import com.red.ink.dto.ResponseDto;
import com.red.ink.jsonconstractor.JsonConstractor;
import com.red.ink.model.EventUpload;
import com.red.ink.model.User;
import com.red.ink.repository.EventUploadRepository;
import com.red.ink.repository.UserRepository;
import com.red.ink.service.EventUploadService;

@Service
public class EventUploadServiceImpl implements EventUploadService {

	@Autowired
	private EventUploadRepository eventUploadRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	private BcryptPasswordEncoder bcryptPasswordEncoder;

	JsonConstractor jsonConstactor = new JsonConstractor();

	@Override
	public ResponseEntity<Object> eventSave(EventUpload eventUpload, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (loggedInUser.isPresent()) {
				if (eventUpload != null) {
					EventUpload eventUpload2 = new EventUpload();
					eventUpload2.setEventName(eventUpload.getEventName());
					eventUpload2.setEventDate(eventUpload.getEventDate());
					eventUpload2.setEventPlace(eventUpload.getEventPlace());
					eventUpload2.setImage(eventUpload.getImage());
					eventUpload2.setUser(loggedInUser.get());

//					if(eventUpload.getEventDate()!=null) {
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
//					Date eventDate2 = sdf.parse(eventUpload.getEventDate().toString()); 
//		               		              
//		               // eventUpload2.setEventDate(eventDate2);
//				}

					// String To Date Convert
					if (eventUpload.getEventDate() != null) {

						String sDate1 = eventUpload.getEventDate();
						Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
						// Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

						// eventUpload2.setEventDate(eventDate2);
					}

					if (eventUpload.getImage() != null && eventUpload.getImage() != "") {
						String imageUser = eventUpload.getImage();

						// if (user.getProfile() != null && user.getProfile() != "") {
						String fileName = eventUpload.getImage().substring(eventUpload.getImage().lastIndexOf("/"));
						String filePath = Constants.USERPROFILEIMG + fileName;
						File dest = new File(filePath);
						dest.delete();

						// }
						String path = null;
						boolean base64 = false;
						// photo path can send data start that only for Base64 datas
						if (eventUpload.getImage().startsWith("data:")) {
							String[] arrOfStr = eventUpload.getImage().split(",");
							String[] arrOfStr1 = arrOfStr[0].split(";");
							if (arrOfStr1[1].equalsIgnoreCase("base64")) {
								base64 = true;
							} else {
								base64 = false;
							}
						}
						if (base64) {

							String[] path1 = eventUpload.getImage().split("/");
							String fileName1 = path1[path1.length - 1];
							Path myPath = Paths.get(Constants.EVENTUPLOADIMG + "/" + fileName1);
							try {
								Files.delete(myPath);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							String[] parts = eventUpload.getImage().split(",");
							String imageString = parts[1];
							try {
								File dir = new File(Constants.EVENTUPLOADIMG);
								if (!dir.exists()) {
									dir.mkdirs();
								}
								BufferedImage image = null;
								Decoder decoder = Base64.getDecoder();
								byte[] resultImage = decoder.decode(imageString);
								ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
								image = ImageIO.read(bais);
								bais.close();

								String fileName11 = eventUpload2.getEventPlace() + eventUpload2.getEventName()
										+ UUID.randomUUID().toString() + "." + "png";
								String filePath1 = Constants.EVENTUPLOADIMG + fileName11;
								File outputFile = new File(filePath1);
								ImageIO.write(image, "png", outputFile);
								path = Constants.EVENTUPLOADIMG_ACCESSPATH + fileName11;
								eventUpload2.setImage(path);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					eventUploadRepository.save(eventUpload2);
					dto.setEvent(eventUpload2);

					return jsonConstactor.responseCreation(true, "Event Upload", "Success", dto);
					//

				}

			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ResponseEntity<Object> eventLists(String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (loggedInUser.isPresent()) {

				List<EventUpload> eventUploads = eventUploadRepository.findAll();
				List<EventUpload> listEventUploads = new ArrayList<EventUpload>();
				if (eventUploads.size() > 0) {

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				

					Comparator<EventUpload> compareByFromTime = (EventUpload b1, EventUpload b2) -> b1.getEventDate()
							.compareTo(b2.getEventDate());
					Collections.sort(eventUploads, compareByFromTime);

					for (EventUpload eventUpload1 : eventUploads) {

						

						// end Date
						Date eventDate2 = sdf.parse(eventUpload1.getEventDate().toString());
						
						String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						Date currentDate1 = sdf.parse(currentDate);
						String currentDate2 = sdf.format(currentDate1);
						Date currentDate3 = sdf.parse(currentDate2.toString());

						if (!(currentDate3.after(eventDate2))) {
							
							if ((currentDate3.before(eventDate2) || currentDate3.equals(eventDate2))) {
								if(loggedInUser.get().getRole().getRole().equals("ADMIN") || loggedInUser.get().equals(eventUpload1.getUser())) {
									eventUpload1.setDeleteStatus(true);
									eventUpload1.setEditstatus(true);
								}else {
									eventUpload1.setDeleteStatus(false);
									eventUpload1.setEditstatus(false);
								}
								listEventUploads.add(eventUpload1);


							}

						

							dto.setEventList(listEventUploads);
						}
					}
					return jsonConstactor.responseCreation(true, "Event Details Get Sucess", null, dto);

				} else {
					return jsonConstactor.responseCreation(true, "Event List is empty", null, dto);
				}
			} else {
				return jsonConstactor.responseCreation(false, "un Authrozied", null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public ResponseEntity<Object> eventUpdate(EventUpload eventUpload, String token) {

		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (eventUpload.getId() != null) {
				Optional<EventUpload> eventUpload1 = eventUploadRepository.findById(eventUpload.getId());

				if (loggedInUser.isPresent()) {
					if (loggedInUser.get().getRole().getRole().equals("ADMIN")) {
						if (eventUpload != null && eventUpload.getId() != null) {
							EventUpload eventUpload2 = new EventUpload();
							eventUpload2.setEventName(eventUpload.getEventName());
							eventUpload2.setEventDate(eventUpload.getEventDate());
							eventUpload2.setEventPlace(eventUpload.getEventPlace());
							eventUpload2.setId(eventUpload.getId());
							eventUpload2.setUser(eventUpload1.get().getUser());
							if (eventUpload1.get().getImage() == null || eventUpload1.get().getImage() == "") {
								if (eventUpload.getImage() != null && eventUpload.getImage() != "") {
									String imageUser = eventUpload.getImage();

									String fileName = eventUpload1.get().getImage();
									String filePath = Constants.USERPROFILEIMG + fileName;
									File dest = new File(filePath);
									dest.delete();

									String path = null;
									boolean base64 = false;
									if (eventUpload.getImage().startsWith("data:")) {
										String[] arrOfStr = eventUpload.getImage().split(",");
										String[] arrOfStr1 = arrOfStr[0].split(";");
										if (arrOfStr1[1].equalsIgnoreCase("base64")) {
											base64 = true;
										} else {
											base64 = false;
										}
									}
									if (base64) {

										String[] path1 = eventUpload.getImage().split("/");
										String fileName1 = path1[path1.length - 1];
										Path myPath = Paths.get(Constants.EVENTUPLOADIMG + "/" + fileName1);
										try {
											Files.delete(myPath);
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

										String[] parts = eventUpload.getImage().split(",");
										String imageString = parts[1];
										try {
											File dir = new File(Constants.EVENTUPLOADIMG);
											if (!dir.exists()) {
												dir.mkdirs();
											}
											BufferedImage image = null;
											Decoder decoder = Base64.getDecoder();
											byte[] resultImage = decoder.decode(imageString);
											ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
											image = ImageIO.read(bais);
											bais.close();

											String fileName11 = eventUpload2.getEventPlace()
													+ eventUpload2.getEventName() + UUID.randomUUID().toString() + "."
													+ "png";
											String filePath1 = Constants.EVENTUPLOADIMG + fileName11;
											File outputFile = new File(filePath1);
											ImageIO.write(image, "png", outputFile);
											path = Constants.EVENTUPLOADIMG_ACCESSPATH + fileName11;
											eventUpload2.setImage(path);

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							} else {
								if (eventUpload1.get().getImage().equals(eventUpload.getImage())) {
									eventUpload2.setImage(eventUpload.getImage());
								} else {
									if (eventUpload.getImage() != null && eventUpload.getImage() != "") {
										String imageUser = eventUpload.getImage();

										if (eventUpload1.get().getImage() != null
												&& !eventUpload1.get().getImage().isEmpty()) {
											String filePath1 = Constants.EVENTUPLOADIMG
													+ extractFileName(eventUpload1.get().getImage());
											File imageFile = new File(filePath1);
											if (imageFile.exists()) {
												imageFile.delete();
											}
										}

										String fileName = eventUpload1.get().getImage();
										String filePath = Constants.USERPROFILEIMG + fileName;
										File dest = new File(filePath);
										dest.delete();

										String path = null;
										boolean base64 = false;
										// photo path can send data start that only for Base64 datas
										if (eventUpload.getImage().startsWith("data:")) {
											String[] arrOfStr = eventUpload.getImage().split(",");
											String[] arrOfStr1 = arrOfStr[0].split(";");
											if (arrOfStr1[1].equalsIgnoreCase("base64")) {
												base64 = true;
											} else {
												base64 = false;
											}
										}
										if (base64) {

											String[] path1 = eventUpload.getImage().split("/");
											String fileName1 = path1[path1.length - 1];
											Path myPath = Paths.get(Constants.EVENTUPLOADIMG + "/" + fileName1);
											try {
												Files.delete(myPath);
											} catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}

											String[] parts = eventUpload.getImage().split(",");
											String imageString = parts[1];
											try {
												File dir = new File(Constants.EVENTUPLOADIMG);
												if (!dir.exists()) {
													dir.mkdirs();
												}
												BufferedImage image = null;
												Decoder decoder = Base64.getDecoder();
												byte[] resultImage = decoder.decode(imageString);
												ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
												image = ImageIO.read(bais);
												bais.close();

												String fileName11 = eventUpload2.getEventPlace()
														+ eventUpload2.getEventName() + UUID.randomUUID().toString()
														+ "." + "png";
												String filePath1 = Constants.EVENTUPLOADIMG + fileName11;
												File outputFile = new File(filePath1);
												ImageIO.write(image, "png", outputFile);
												path = Constants.EVENTUPLOADIMG_ACCESSPATH + fileName11;
												eventUpload2.setImage(path);

											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}

								}
							}

							eventUploadRepository.saveAndFlush(eventUpload2);
							dto.setEvent(eventUpload2);
							return jsonConstactor.responseCreation(true, "Event Update", "Success", dto);
						}

					} else if (loggedInUser.get().equals(eventUpload1.get().getUser())) {
						if (eventUpload != null && eventUpload.getId() != null) {
							EventUpload eventUpload2 = new EventUpload();
							eventUpload2.setEventName(eventUpload.getEventName());
							eventUpload2.setEventDate(eventUpload.getEventDate());
							eventUpload2.setEventPlace(eventUpload.getEventPlace());
							eventUpload2.setUser(eventUpload1.get().getUser());
							eventUpload2.setId(eventUpload.getId());
							if (eventUpload1.get().getImage() == null || eventUpload1.get().getImage() == "") {
								if (eventUpload.getImage() != null && eventUpload.getImage() != "") {
									String imageUser = eventUpload.getImage();

									String fileName = eventUpload1.get().getImage();
									String filePath = Constants.USERPROFILEIMG + fileName;
									File dest = new File(filePath);
									dest.delete();

									String path = null;
									boolean base64 = false;
									if (eventUpload.getImage().startsWith("data:")) {
										String[] arrOfStr = eventUpload.getImage().split(",");
										String[] arrOfStr1 = arrOfStr[0].split(";");
										if (arrOfStr1[1].equalsIgnoreCase("base64")) {
											base64 = true;
										} else {
											base64 = false;
										}
									}
									if (base64) {

										String[] path1 = eventUpload.getImage().split("/");
										String fileName1 = path1[path1.length - 1];
										Path myPath = Paths.get(Constants.EVENTUPLOADIMG + "/" + fileName1);
										try {
											Files.delete(myPath);
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

										String[] parts = eventUpload.getImage().split(",");
										String imageString = parts[1];
										try {
											File dir = new File(Constants.EVENTUPLOADIMG);
											if (!dir.exists()) {
												dir.mkdirs();
											}
											BufferedImage image = null;
											Decoder decoder = Base64.getDecoder();
											byte[] resultImage = decoder.decode(imageString);
											ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
											image = ImageIO.read(bais);
											bais.close();

											String fileName11 = eventUpload2.getEventPlace()
													+ eventUpload2.getEventName() + UUID.randomUUID().toString() + "."
													+ "png";
											String filePath1 = Constants.EVENTUPLOADIMG + fileName11;
											File outputFile = new File(filePath1);
											ImageIO.write(image, "png", outputFile);
											path = Constants.EVENTUPLOADIMG_ACCESSPATH + fileName11;
											eventUpload2.setImage(path);

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							} else {
								if (eventUpload1.get().getImage().equals(eventUpload.getImage())) {
									eventUpload2.setImage(eventUpload.getImage());
								} else {
									if (eventUpload.getImage() != null && eventUpload.getImage() != "") {
										String imageUser = eventUpload.getImage();

										if (eventUpload1.get().getImage() != null
												&& !eventUpload1.get().getImage().isEmpty()) {
											String filePath1 = Constants.EVENTUPLOADIMG
													+ extractFileName(eventUpload1.get().getImage());
											File imageFile = new File(filePath1);
											if (imageFile.exists()) {
												imageFile.delete();
											}
										}

										String fileName = eventUpload1.get().getImage();
										String filePath = Constants.USERPROFILEIMG + fileName;
										File dest = new File(filePath);
										dest.delete();

										String path = null;
										boolean base64 = false;
										// photo path can send data start that only for Base64 datas
										if (eventUpload.getImage().startsWith("data:")) {
											String[] arrOfStr = eventUpload.getImage().split(",");
											String[] arrOfStr1 = arrOfStr[0].split(";");
											if (arrOfStr1[1].equalsIgnoreCase("base64")) {
												base64 = true;
											} else {
												base64 = false;
											}
										}
										if (base64) {

											String[] path1 = eventUpload.getImage().split("/");
											String fileName1 = path1[path1.length - 1];
											Path myPath = Paths.get(Constants.EVENTUPLOADIMG + "/" + fileName1);
											try {
												Files.delete(myPath);
											} catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}

											String[] parts = eventUpload.getImage().split(",");
											String imageString = parts[1];
											try {
												File dir = new File(Constants.EVENTUPLOADIMG);
												if (!dir.exists()) {
													dir.mkdirs();
												}
												BufferedImage image = null;
												Decoder decoder = Base64.getDecoder();
												byte[] resultImage = decoder.decode(imageString);
												ByteArrayInputStream bais = new ByteArrayInputStream(resultImage);
												image = ImageIO.read(bais);
												bais.close();

												String fileName11 = eventUpload2.getEventPlace()
														+ eventUpload2.getEventName() + UUID.randomUUID().toString()
														+ "." + "png";
												String filePath1 = Constants.EVENTUPLOADIMG + fileName11;
												File outputFile = new File(filePath1);
												ImageIO.write(image, "png", outputFile);
												path = Constants.EVENTUPLOADIMG_ACCESSPATH + fileName11;
												eventUpload2.setImage(path);

											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}

								}
							}

							eventUploadRepository.saveAndFlush(eventUpload2);
							dto.setEvent(eventUpload2);
							return jsonConstactor.responseCreation(true, "Event Update", "Success", dto);
						}
					}

				}

			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ResponseEntity<Object> eventDelete(String token, Long id) {
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			Optional<EventUpload>eventUpload=eventUploadRepository.findById(id);
			if(eventUpload.isPresent()) {
				if(loggedInUser.get().getRole().getRole().equals("ADMIN")) {
					eventUploadRepository.deleteById(id);
					return jsonConstactor.responseCreation(true, "Event Delete", "Success", null, null);
				}else if(loggedInUser.get().equals(eventUpload.get().getUser())) {
					
					eventUploadRepository.deleteById(id);
					return jsonConstactor.responseCreation(true, "Event Delete", "Success", null, null);
				}
				else {
					return jsonConstactor.responseCreation(false, "Event cant'be delete ", "fail", null, null);
				}
			}
			

			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> finById(Long id, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			Optional<EventUpload> event = eventUploadRepository.findById(id);

			dto.setEvent(event.get());

			return jsonConstactor.responseCreation(true, "event get sucess", null, dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> removeEventImage(Long eventId, String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		try {
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (loggedInUser.isPresent()) {
				Optional<EventUpload> event = eventUploadRepository.findById(eventId);

				if (!event.isEmpty()) {
					if(loggedInUser.get().getRole().getRole().equals("ADMIN") || loggedInUser.get().equals(event.get().getUser())) {
						// Delete the image file from the local storage
						if (event.get().getImage() != null && !event.get().getImage().isEmpty()) {
							String filePath1 = Constants.EVENTUPLOADIMG + extractFileName(event.get().getImage());
							File imageFile = new File(filePath1);
							if (imageFile.exists()) {
								imageFile.delete();
							}

							// Set the image field to null
							event.get().setImage(null);
						}
						eventUploadRepository.save(event.get());

						return jsonConstactor.responseCreation(true, "Event image Delete", "Success", null, null);
					}
//					else if(loggedInUser.get().equals(event.get().getUser())) {
//						// Delete the image file from the local storage
//						if (event.get().getImage() != null && !event.get().getImage().isEmpty()) {
//							String filePath1 = Constants.EVENTUPLOADIMG + extractFileName(event.get().getImage());
//							File imageFile = new File(filePath1);
//							if (imageFile.exists()) {
//								imageFile.delete();
//							}
//
//							// Set the image field to null
//							event.get().setImage(null);
//						}
//					}
					
					return jsonConstactor.responseCreation(false, "Event image can't be  Delete", "failed", null, null);
				} else {
					throw new Exception("Event not found with id " + eventId);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private String extractFileName(String path) {
		// Assuming the image path is in the format:
		// "http://localhost/EventUploadImg/filename.png"
		return path.substring(path.lastIndexOf('/') + 1);
	}

	@Override
	public ResponseEntity<Object> getEventsUserBased(String token, Long userId) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		ResponseDto dto = new ResponseDto();
		try {
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if(loggedInUser.isPresent()) {
				List<EventUpload>list=new ArrayList<>();
				Optional<User>user=userRepository.findById(userId);
				if(loggedInUser.get().getRole().getRole().equals("ADMIN") && user.get().getRole().getRole().equals("ADMIN")) {
					List<EventUpload>eventUploads=eventUploadRepository.findAll();
					list=eventUploads.stream()
				              .peek(event -> {
				            	  event.setDeleteStatus(true);
				            	  event.setEditstatus(true);
				              })
				              .collect(Collectors.toList());
				}else {
					
					if(user.isPresent()) {
						List<EventUpload>eventUploads=eventUploadRepository.findByUser(user.get());
						list=eventUploads.stream()
								              .peek(event -> {
								            	  event.setDeleteStatus(true);
								            	  event.setEditstatus(true);
								              })
								              .collect(Collectors.toList());
				}
			
					
				}
				
				if(list.size() > 0) {
					dto.setEventList(list);
				}
				
				return jsonConstactor.responseCreation(true, "event get sucess", null, dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
