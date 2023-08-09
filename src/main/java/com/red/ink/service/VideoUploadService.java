/**
 * 
 */
package com.red.ink.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.red.ink.model.VideoUpload;

/**
 * @author Ajith
 *
 */
public interface VideoUploadService {

//	ResponseEntity<Object> uploadVideo(String token, MultipartFile videoFile, VideoUpload videoUpload);

	ResponseEntity<Object> uploadVideo(String token, MultipartFile videoFile, String description, String title);

	ResponseEntity<FileSystemResource> downloadVideo(String token, Long id, HttpServletRequest request, HttpServletResponse response);

	ResponseEntity<FileSystemResource> videosDirectDownload(String token, Long id, HttpServletRequest request,
			HttpServletResponse response);

	String viewGetUrl(String token, Long id);

	ResponseEntity<Object> getAllVideoFileNames(String token);

	ResponseEntity<Object> uploadVideoLists(String token, MultipartFile[] videoFiles, String description, String title);

}
