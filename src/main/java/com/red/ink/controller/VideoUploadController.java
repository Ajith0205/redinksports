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

	/*
	 * @Param File  to send Video File
	 * @ upload only Mp4 Format
	 * 
	 *
	*/
	@PostMapping(value = "upload")
	public ResponseEntity<Object>uploadVideo(@RequestHeader("Authorization") String token,@RequestParam("file") MultipartFile videoFile, @RequestParam("title") String title,@RequestParam("description") String description){
		return videoUploadService.uploadVideo(token,videoFile,description,title);
	}
	
	/*
	 * @Param File  to send Video File in Array
	 * @ upload only Mp4 Format
	 * 
	 *
	*/
	@PostMapping(value = "uploadVideos")
	public ResponseEntity<Object>uploadVideoLists(@RequestHeader("Authorization") String token,@RequestParam("file") MultipartFile videoFiles[], @RequestParam("title") String title,@RequestParam("description") String description){
		return videoUploadService.uploadVideoLists(token,videoFiles,description,title);
	}
	
	/*
	 * @Param  Pathvariable using  directly send id  (url/video/id)
	 * @ download view Directly
	 * 
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
	
	
	/*
	 * @Param  Pathvariable using  directly send id  (url/video/id)
	 * @ download video Directly
	 * 
	*/
	
	 @GetMapping("downloadVideo/{id}")
	    public ResponseEntity<FileSystemResource> downloadVideo(@RequestHeader("Authorization") String token, @PathVariable Long id,
	                                                            HttpServletRequest request,
	                                                            HttpServletResponse response){
			return videoUploadService.videosDirectDownload(token,id,request,response);
		 
	 }
	 
	 
	 @GetMapping("videoList")
	 public ResponseEntity<Object> getAllVideoFileNames(@RequestHeader("Authorization") String token) {
		return videoUploadService.getAllVideoFileNames(token);
		 
	 }
	 
	 
	

}
