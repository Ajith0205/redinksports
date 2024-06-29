/**
 * 
 */
package com.red.ink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.red.ink.service.VideoUploadService;

/**
 * @author Ajith
 *
 */

@RestController
@RequestMapping("/videoUpload/")
public class VideoUploadController {
	
	@Autowired
	private VideoUploadService videoUploadService;

	
	/**
	 * Upload Video
	 * 
	 * @param token
	 * @param videoFile
	 * @param title
	 * @param description
	 * @return
	 */
	@PostMapping(value = "upload")
	public ResponseEntity<Object>uploadVideo(@RequestHeader("Authorization") String token,@RequestParam("file") MultipartFile videoFile, @RequestParam("title") String title,@RequestParam("description") String description){
		return videoUploadService.uploadVideo(token,videoFile,description,title);
	}
	
/**
 * upload Videos List
 * 
 * @param token
 * @param videoFiles
 * @param title
 * @param description
 * @return
 */
	@PostMapping(value = "uploadVideos")
	public ResponseEntity<Object>uploadVideoLists(@RequestHeader("Authorization") String token,@RequestParam("file") MultipartFile videoFiles[], @RequestParam("title") String title,@RequestParam("description") String description){
		return videoUploadService.uploadVideoLists(token,videoFiles,description,title);
	}
	
	/**
	 * download view Directly (url/video/id)
	 * 
	 * @param token
	 * @param id
	 * @return
	 */
	@GetMapping("videos/{id}")
	
	public String viewVideo(@RequestHeader("Authorization") String token,@PathVariable("id") Long id){
		return videoUploadService.viewGetUrl(token,id);
		
	}
	
//	
//    public ResponseEntity<FileSystemResource> viewVideo(@RequestHeader("Authorization") String token,@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
//		
//		return videoUploadService.downloadVideo(token,id,request,response);
//		
//	}
	

	
	/**
	 * download video Directly (url/video/id)
	 * 
	 * @param token
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	
	 @GetMapping("downloadVideo/{id}")
	    public ResponseEntity<FileSystemResource> downloadVideo(@RequestHeader("Authorization") String token, @PathVariable Long id,
	                                                            HttpServletRequest request,
	                                                            HttpServletResponse response){
			return videoUploadService.videosDirectDownload(token,id,request,response);
		 
	 }
	 
	 /**
	  * get Video List
	  * 
	  * @param token
	  * @return
	  */
	 @GetMapping("videoList")
	 public ResponseEntity<Object> getAllVideoFileNames(@RequestHeader("Authorization") String token) {
		return videoUploadService.getAllVideoFileNames(token);
		 
	 }
	 /**
	  * user Based Video get
	  * @param token
	  * @param userId
	  * @return
	  */
	 @GetMapping("userBasedVideos")
	 public ResponseEntity<Object> getUserBasedVideos(@RequestHeader("Authorization") String token,@RequestParam Long userId) {
		return videoUploadService.getUserBasedVideos(token,userId);
		 
	 }
	 
	 
	 /**
	  * delete video
	  * 
	  * @param token
	  * @param id
	  * @return
	  */
	 
	 @DeleteMapping("delete/{id}")
	 public ResponseEntity<Object>deleteVideo(@RequestHeader("Authorization") String token,@PathVariable Long id){
		return videoUploadService.deleteByVideo(token,id);
		 
	 }
	 
	 
	 
	

}
