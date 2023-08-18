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
public class EventUploadServiceImpl implements EventUploadService{

	
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
				if(eventUpload !=null) {
					EventUpload eventUpload2=new EventUpload();
					eventUpload2.setEventName(eventUpload.getEventName());
					eventUpload2.setEventDate(eventUpload.getEventDate());
					eventUpload2.setEventPlace(eventUpload.getEventPlace());
					eventUpload2.setImage(eventUpload.getImage());
//					if(eventUpload.getEventDate()!=null) {
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
//					Date eventDate2 = sdf.parse(eventUpload.getEventDate().toString()); 
//		               		              
//		               // eventUpload2.setEventDate(eventDate2);
//				}
					
					//String To Date Convert
					if(eventUpload.getEventDate()!=null) {
					
						
						String sDate1=eventUpload.getEventDate(); 
						  Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);  
						//  Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
			               		              
			               // eventUpload2.setEventDate(eventDate2);
					}
					
					
					
					if(eventUpload.getImage() !=null&&eventUpload.getImage() !="") {
						String imageUser = eventUpload.getImage();

					//	if (user.getProfile() != null && user.getProfile() != "") {
							String fileName = eventUpload.getImage().substring(eventUpload.getImage().lastIndexOf("/"));
							String filePath = Constants.USERPROFILEIMG + fileName;
							File dest = new File(filePath);
							dest.delete();

					//	}
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

								String fileName11 =eventUpload2.getEventPlace()+eventUpload2.getEventName()+UUID.randomUUID().toString() + "." + "png";
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
                    
                    return jsonConstactor.responseCreation(true,"Event Upload","Success", dto);
					//
					
				}
				
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		
		
		return null;
	}

	@Override
	public ResponseEntity<Object> eventLists( String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (loggedInUser.isPresent()) {
				
				List<EventUpload>eventUploads=eventUploadRepository.findAll();
//				List<EventUpload> listEventUploads = eventUploadRepository
//						.findByFamilyCode(loggedInUser.get(), Sort.by(Sort.Direction.ASC, "startDate"));
				List<EventUpload> listEventUploads= new ArrayList<EventUpload>();
				if (eventUploads.size() > 0) {
					
					SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
				   
					//Ascending order Date  below Condtion all is working
					
			//1		eventUploads.sort(Comparator.comparing(EventUpload::getEventDate));
					
			
					
	//2				eventUploads.sort((o1, o2) -> (o1.getEventDate()) .compareTo(o2.getEventDate()));
		                   
					
//	3				eventUploads.stream().sorted((o1, o2) -> (o1.getEventDate())
//		                    .compareTo(o2.getEventDate())).collect(Collectors.toList());
//					

		
					 

	//4
					
					Comparator<EventUpload> compareByFromTime = (EventUpload b1, EventUpload b2) -> b1.getEventDate()
							.compareTo(b2.getEventDate());
					Collections.sort(eventUploads, compareByFromTime);
					
					
					
				for (EventUpload eventUpload1 : eventUploads) {
						
				//	for (EventUpload eventUpload1 : eventUploadAsending) {
						
						// end Date
						Date eventDate2 = sdf.parse(eventUpload1.getEventDate().toString());
						//start date
						//Date startDate2 = sdf.parse(runningMessage.getStartDate().toString());
						//current date 
						String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						Date currentDate1 = sdf.parse(currentDate);
						String currentDate2 = sdf.format(currentDate1);
						Date currentDate3 = sdf.parse(currentDate2.toString());
					
						if (!(currentDate3.after(eventDate2))) {
						//	if (!(currentDate3.before(startDate2))) {
								if ((currentDate3.before(eventDate2) || currentDate3.equals(eventDate2) )) {
									listEventUploads.add(eventUpload1);
//									System.out.println(eventUpload1);
							
								}
								
						//	}
								
								dto.setEventList(listEventUploads);
						}
					}
					return jsonConstactor.responseCreation(true,"Event Details Get Sucess",null,dto);

				} else {
					return jsonConstactor.responseCreation(true,"Event List is empty",null, null);
				}
			} else {
				return jsonConstactor.responseCreation(false,"un Authrozied",null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> eventUpdate(EventUpload eventUpload, Long id, String token) {
		
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if(id!=null) {
				Optional<EventUpload>eventUpload1=eventUploadRepository.findById(id);
			
			if (loggedInUser.isPresent()) {
				if(eventUpload !=null && eventUpload.getId() !=null) {
					EventUpload eventUpload2=new EventUpload();
					eventUpload2.setEventName(eventUpload.getEventName());
					eventUpload2.setEventDate(eventUpload.getEventDate());
					eventUpload2.setEventPlace(eventUpload.getEventPlace());
					eventUpload2.setId(id);
				
					if(eventUpload1.get().getImage().equals(eventUpload.getImage())) {
						eventUpload2.setImage(eventUpload.getImage());
					}else {
						if(eventUpload.getImage() !=null&&eventUpload.getImage() !="") {
							String imageUser = eventUpload.getImage();

						//	if (user.getProfile() != null && user.getProfile() != "") {
//								String fileName = eventUpload.getImage().substring(eventUpload.getImage().lastIndexOf("/"));
						     	String fileName=eventUpload1.get().getImage();
								String filePath = Constants.USERPROFILEIMG + fileName;
								File dest = new File(filePath);
								dest.delete();

						//	}
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

									String fileName11 =eventUpload2.getEventPlace()+eventUpload2.getEventName()+ "." + "png";
									String filePath1 = Constants.EVENTUPLOADIMG + fileName11;
									File outputFile = new File(filePath1);
									ImageIO.write(image, "png", outputFile);
									path = Constants.EVENTUPLOADIMG_ACCESSPATH + fileName11;
									eventUpload2.setImage(path);
	                          //  eventUploadRepository.saveAndFlush(eventUpload2);
	                         
	                            
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						
					}
					
					
					  eventUploadRepository.saveAndFlush(eventUpload2);
					  dto.setEvent(eventUpload2);
					  return jsonConstactor.responseCreation(true,"Event Update","Success", dto);
				}
				
				
			}
			
		}
			
			return null;
		}catch(Exception e) {
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
			
			eventUploadRepository.deleteById(id);
			
			return jsonConstactor.responseCreation(true,"Event Delete","Success", null, null);
		}catch(Exception e) {
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
		Optional<EventUpload> event=eventUploadRepository.findById(id);
		
		
		dto.setEvent(event.get());
		
			return jsonConstactor.responseCreation(true,"event get sucess",null, dto);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	
	
	
	

	
}
