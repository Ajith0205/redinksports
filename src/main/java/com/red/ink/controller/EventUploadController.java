package com.red.ink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.red.ink.jsonconstractor.JsonConstractor;
import com.red.ink.model.EventUpload;
import com.red.ink.service.EventUploadService;

@RestController
@RequestMapping("/event/")
public class EventUploadController {

	@Autowired
	private EventUploadService eventUploadService;

	
	 
	
	 /**
	  * create Event
	  * 
	  * @param eventUpload
	  * @param token
	  * @return
	  */
	 @PostMapping("upload")
		public ResponseEntity<Object> eventUploadSave(@RequestBody EventUpload eventUpload,
				@RequestHeader("Authorization") String token) {
			return eventUploadService.eventSave(eventUpload, token);
		}
	 
	 
	 /**
	  * upload List Of events
	  * 
	  * @param token
	  * @return
	  */
	 @GetMapping("uploadList")
	 public ResponseEntity<Object> eventUploadList(
				@RequestHeader("Authorization") String token) {
			return eventUploadService.eventLists( token);
		}
	 /**
	  * update Event
	  * 
	  * @param eventUpload
	  * @param id
	  * @param token
	  * @return
	  */
	 @PutMapping("update")
	 public ResponseEntity<Object>updateEvent(@RequestBody EventUpload eventUpload,
			 @RequestHeader("Authorization") String token){
		 
				return eventUploadService.eventUpdate(eventUpload,token);
		 
	 }
	 
	 /**
	  * delete event
	  * @param token
	  * @param id
	  * @return
	  */
	 @DeleteMapping("eventdelete")
	 public ResponseEntity<Object>deleteEvent(@RequestHeader("Authorization") String token,@RequestParam("id") Long id){
		return eventUploadService.eventDelete(token,id);
		 
	 }
	 
	 /**
	  * find By event 
	  * @param id
	  * @param token
	  * @return
	  */
	 
	 @GetMapping("findeventid")
	 
	 public ResponseEntity<Object>findByEventId(@RequestParam("id")Long id,@RequestHeader("Authorization") String token){
		return eventUploadService.finById(id,token);
		 
	 }
	 
	 
	 /**
	  * Event Image only delete
	  * @param eventId
	  * @param token
	  * @return
	  */
	  @DeleteMapping("image/{eventId}")
	    public ResponseEntity<Object> deleteEventImage(@PathVariable Long eventId,@RequestHeader("Authorization") String token) {
		  return eventUploadService.removeEventImage(eventId,token);
	    }
	 /**
	  * Event GetUser Based
	  * @param token
	  * @param userId
	  * @return
	  */
	 
	 @GetMapping("user/eventGet")
	 public ResponseEntity<Object>userBasedEventGet(@RequestHeader("Authorization") String token,@RequestParam Long userId){
		return eventUploadService.getEventsUserBased(token,userId);
		 
	 }
	 
//		@Value("${file.upload.dir}")
//		  String FILE_DIRECTORY;
		
		
//		@PostMapping("upload")
//		public ResponseEntity<Object>profileUpload(@RequestParam("file")MultipartFile file,@RequestHeader("Authorization")String token){
//			String jwtToken = token.replaceFirst("Bearer", "");
//			DecodedJWT jwt = JWT.decode(jwtToken.trim());
//			Optional<User> user= user.findByUsername(jwt.getSubject());
//			
//			if(user !=null) {
//				File myFile=new File(FILE_DIRECTORY+file.getOriginalFilename());
//				
//				ProfileUpload profile=new ProfileUpload();
//				
//				try {
//					myFile.createNewFile();
//					//file output Stream File Writing
//					FileOutputStream outputStream=new FileOutputStream(myFile);
//					
//					outputStream.write(file.getBytes());
//					profile.setImage(file.getOriginalFilename());
//					profile.setUser(user.get());
//					profileUploadService.save(profile);
//					
//					outputStream.flush();
//					outputStream.close();
//					
//				}catch(IOException i) {
//					i.printStackTrace();
//				}
//				return jsonConstactor.responseCreation(true,"profile Upload", "Sucess", null, null);
//				
//				
//			}
//			
//			 
//			return jsonConstactor.responseCreation(false,"User is", "not valid", null, null);
//			
//		}
	 

}
