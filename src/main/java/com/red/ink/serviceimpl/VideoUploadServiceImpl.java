/**
 * 
 */
package com.red.ink.serviceimpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.util.Value;
import com.red.ink.constant.Constants;
import com.red.ink.dto.ResponseDto;
import com.red.ink.jsonconstractor.JsonConstractor;
import com.red.ink.model.User;
import com.red.ink.model.VideoUpload;
import com.red.ink.repository.UserRepository;
import com.red.ink.repository.VideoUploadRepository;
import com.red.ink.service.VideoUploadService;
import org.springframework.core.env.Environment;

/**
 * @author Ajith
 *
 */
@Service
@PropertySource("classpath:application.properties")
public class VideoUploadServiceImpl implements VideoUploadService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	VideoUploadRepository videoUploadRepository;

//	@Value("${upload.video.path}")
//    private String uploadVideoPath;

	private String uploadVideoPath;

	@Autowired
	private Environment environment;

	@PostConstruct
	public void init() {
		uploadVideoPath = environment.getProperty("upload.video.path");
	}

	private static final int BUFFER_SIZE = 10000;

	JsonConstractor jsonConstractor = new JsonConstractor();

	@Override
	public ResponseEntity<Object> uploadVideo(String token, MultipartFile videoFile, String description, String title) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if (loggedInUser.isPresent()) {

			String originalFilename = videoFile.getOriginalFilename();
			// Check if the file is an MP4 video
			// Check if the file name ends with .mp4 (case-insensitive)
			if (!originalFilename.toLowerCase().endsWith(".mp4")) {
				return jsonConstractor.responseCreation(false, "Invalid video format. Only MP4 videos are allowed.",
						null, null);
			}

			Path path = Paths.get(originalFilename);
			String fileNameWithoutDirectories = path.getFileName().toString();
			String extension = "";

			// Extract the file extension if available
			int extensionIndex = fileNameWithoutDirectories.lastIndexOf(".mp4");
			boolean videoStatus;

			String desiredPart = null;
			String lastPart = null;
			if (extensionIndex != -1) {
				desiredPart = originalFilename.substring(0, extensionIndex);
//	                System.out.println("Desired part before .mp4: " + desiredPart);

				// Find the last index of the space character in the file name
				int lastSpaceIndex = desiredPart.lastIndexOf(" ");

				if (lastSpaceIndex != -1) {
					lastPart = desiredPart.substring(lastSpaceIndex + 1);
//		                System.out.println("Last part of the file name: " + lastPart);
				} else {
					lastPart = desiredPart;
				}

			}

			String uniqueFileName = title + "_" + lastPart + "_" + UUID.randomUUID().toString() + ".mp4";
			String filePath = Constants.UPLOADVIDEO + uniqueFileName;

			// Save the video file to the upload directory
			File file = new File(filePath);
			BufferedOutputStream stream = null;
			try {
				stream = new BufferedOutputStream(new FileOutputStream(file));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				stream.write(videoFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Save video metadata to the database
			VideoUpload videoUpload = new VideoUpload();
			videoUpload.setDescription(description);
			videoUpload.setTitle(title);
			// to get Ui and Database view
			videoUpload.setFileName(Constants.UPLOADVIDEO_ACCESSPATH + uniqueFileName);
			videoUpload.setUser(loggedInUser.get());
			videoUploadRepository.save(videoUpload);

			return jsonConstractor.responseCreation(true, "Video Upload Success", null, null);

		}

		return jsonConstractor.responseCreation(false, "Loggin User is not present", null, null);
	}

	@Override
	public ResponseEntity<FileSystemResource> downloadVideo(String token, Long id, HttpServletRequest request,
			HttpServletResponse response) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if (loggedInUser.isPresent()) {
			if (id != null) {
				Optional<VideoUpload> videoUpload = videoUploadRepository.findById(id);
				if (!videoUpload.isEmpty()) {
					String rootPath = Constants.UPLOADVIDEO;
					String filePath = rootPath + File.separator + videoUpload.get().getFileName();
					File downloadFile = new File(filePath);
					if (downloadFile.exists()) {
						try (FileInputStream inputStream = new FileInputStream(downloadFile);
								OutputStream outStream = response.getOutputStream()) {
							HttpHeaders headers = new HttpHeaders();
							// Set content type based on file extension or default to
							// "application/octet-stream"
							String mimeType = request.getServletContext().getMimeType(downloadFile.getAbsolutePath());
							if (mimeType == null) {
								mimeType = "application/octet-stream";
							}
//		                        response.setContentType(mimeType);

							headers.setContentType(MediaType.valueOf(mimeType));
							return new ResponseEntity<>(new FileSystemResource(downloadFile), headers, HttpStatus.OK);
//		                        return ResponseEntity.ok().build();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						// File not found error
						return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
					}
				} else {
					// Video with the given ID not found error
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}

			}

		}
		return null;

	}

	@Override
	public ResponseEntity<FileSystemResource> videosDirectDownload(String token, Long id, HttpServletRequest request,
			HttpServletResponse response) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if (loggedInUser.isPresent()) {
			if (id != null) {
				Optional<VideoUpload> videoUpload = videoUploadRepository.findById(id);
				if (videoUpload.isPresent()) {
					String fileName = videoUpload.get().getFileName();

					// Extract the file name from the URL
					String fileName1 = FilenameUtils.getName(fileName);
					// Storage Videos
					String filePath = Constants.UPLOADVIDEO + fileName1;
					File downloadFile = new File(filePath);
					// Storage Videos Avilable Or Not Checking
					if (downloadFile.exists()) {
						try (InputStream inputStream = new FileInputStream(downloadFile)) {
							HttpHeaders headers = new HttpHeaders();
							String headerKey = "Content-Disposition";
							// download name mention
							String headerValue = "attachment;filename=\"" + fileName1 + "\"";

							String pragma = "Pragma";
							String pragmaValue = fileName1;
							response.setHeader(headerKey, headerValue);
							response.setHeader(pragma, pragmaValue);
							headers.setContentLength(downloadFile.length());
							FileSystemResource fileResource = new FileSystemResource(downloadFile);
							return new ResponseEntity<>(fileResource, headers, HttpStatus.OK);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						return ResponseEntity.notFound().build();
					}
				} else {
					return ResponseEntity.notFound().build();
				}
			}
		}
		return null;
	}

	@Override
	public String viewGetUrl(String token, Long id) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if (loggedInUser.isPresent()) {
			if (id != null) {
				Optional<VideoUpload> videoUpload = videoUploadRepository.findById(id);
				if (videoUpload.isPresent()) {
					String fileName = videoUpload.get().getFileName();
					Path videoPath = Paths.get(uploadVideoPath, fileName);
					try {
						Resource videoResource = new UrlResource(videoPath.toUri());
						if (videoResource.exists() && videoResource.isReadable()) {
							return videoResource.getURL().toString();

						} else {
//			                return ResponseEntity.notFound().build();
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		return null;
		
	}

	private String getVideoFileUrl(String fileName, Long id) {

		String contextPath = ServletUriComponentsBuilder.fromCurrentContextPath().build().getPath();
		String videoUrl = fileName;
		return videoUrl;
	}

	@Override
	public ResponseEntity<Object> getAllVideoFileNames(String token) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if (loggedInUser.isPresent()) {
			List<VideoUpload> responseUploadsList = new ArrayList<>();
			List<VideoUpload> videoUploadsList = videoUploadRepository.findAll();
			
//			for (VideoUpload videoUpload : videoUploadsList) {
//				if (loggedInUser.get().getRole().getRole().equals("ADMIN")
//						|| loggedInUser.get().equals(videoUpload.getUser())) {
//					
//					videoUpload.setDeleteStatus(true);
//					videoUpload.setEditStatus(true);
//					
//				}else {
//					videoUpload.setDeleteStatus(false);
//					videoUpload.setEditStatus(false);
//				}
//				
//				responseUploadsList.add(videoUpload);
//			}

			if(loggedInUser.get().getRole().getRole().equals("ADMIN")) {
				responseUploadsList=videoUploadsList.stream()
			            .peek(video -> {
			            	video.setDeleteStatus(true);
			            	video.setEditStatus(true);
			            })
			            .collect(Collectors.toList());
				
			}else {
				responseUploadsList=videoUploadsList.stream()
						            .peek(video ->{
						            	if(video.getUser().equals(loggedInUser.get())) {
						            		video.setDeleteStatus(true);
						            		video.setEditStatus(true);
						            	}else {
						            		video.setDeleteStatus(false);
						            		video.setEditStatus(false);
						            	}
						            })
						            .collect(Collectors.toList());
			}
			
			
			
			dto.setVideoUploadsList(responseUploadsList);
			return jsonConstractor.responseCreation(true, "get Video List Sucess", null, dto);
		}
		return jsonConstractor.responseCreation(false, "Loggin User is not present", null, null);
	}

	@Override
	public ResponseEntity<Object> uploadVideoLists(String token, MultipartFile[] videoFiles, String description,
			String title) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if (loggedInUser.isPresent()) {

			for (MultipartFile videoFile : videoFiles) {
				String originalFilename = videoFile.getOriginalFilename();
				// Check if the file is an MP4 video
				// Check if the file name ends with .mp4 (case-insensitive)
				if (!originalFilename.toLowerCase().endsWith(".mp4")) {
					return jsonConstractor.responseCreation(false, "Invalid video format. Only MP4 videos are allowed.",
							null, null);
				}

				Path path = Paths.get(originalFilename);
				String fileNameWithoutDirectories = path.getFileName().toString();
				String extension = "";

				// Extract the file extension if available
				int extensionIndex = fileNameWithoutDirectories.lastIndexOf(".mp4");
				boolean videoStatus;

				String desiredPart = null;
				String lastPart = null;
				if (extensionIndex != -1) {
					desiredPart = originalFilename.substring(0, extensionIndex);
//				                System.out.println("Desired part before .mp4: " + desiredPart);

					// Find the last index of the space character in the file name
					int lastSpaceIndex = desiredPart.lastIndexOf(" ");

					if (lastSpaceIndex != -1) {
						lastPart = desiredPart.substring(lastSpaceIndex + 1);
//					                System.out.println("Last part of the file name: " + lastPart);
					} else {
						lastPart = desiredPart;
					}

				}

				String uniqueFileName = title + "_" + lastPart + "_" + UUID.randomUUID().toString() + ".mp4";
				String filePath = Constants.UPLOADVIDEO + uniqueFileName;

				// Save the video file to the upload directory
				File file = new File(filePath);
				BufferedOutputStream stream = null;
				try {
					stream = new BufferedOutputStream(new FileOutputStream(file));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					stream.write(videoFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Save video metadata to the database
				VideoUpload videoUpload = new VideoUpload();
				videoUpload.setDescription(description);
				videoUpload.setTitle(title);
				videoUpload.setUser(loggedInUser.get());
				// to get Ui and Database view
				videoUpload.setFileName(Constants.UPLOADVIDEO_ACCESSPATH + uniqueFileName);
				videoUploadRepository.save(videoUpload);

			}

			return jsonConstractor.responseCreation(true, "Video Upload Success", null, null);
//			}

		}
		return jsonConstractor.responseCreation(false, "Loggin User is not present", null, null);
	}

	@Override
	public ResponseEntity<Object> deleteByVideo(String token, Long id) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		if (loggedInUser.isPresent()) {
			Optional<VideoUpload> videoUploadOptional = videoUploadRepository.findById(id);

			if (videoUploadOptional.isPresent()) {
				if (loggedInUser.get().getRole().getRole().equals("ADMIN")
						|| loggedInUser.get().equals(videoUploadOptional.get().getUser())) {

					if (videoUploadOptional.get().getFileName() != null
							&& !videoUploadOptional.get().getFileName().isEmpty()) {
						String filePath1 = Constants.UPLOADVIDEO
								+ extractFileName(videoUploadOptional.get().getFileName());
						File imageFile = new File(filePath1);
						if (imageFile.exists()) {
							imageFile.delete();
						}
					}

					videoUploadRepository.deleteById(videoUploadOptional.get().getId());

					return jsonConstractor.responseCreation(true, "Video Delete Success", null, null);

				}else {
					return jsonConstractor.responseCreation(false, "Cant be delete the video", null, null);
				}

			}
		}

		return jsonConstractor.responseCreation(false, "Loggin User is not present", null, null);
	}

	private String extractFileName(String path) {
		// Assuming the image path is in the format:
		// "http://localhost/EventUploadImg/filename.png"
		return path.substring(path.lastIndexOf('/') + 1);
	}

	@Override
	public ResponseEntity<Object> getUserBasedVideos(String token, Long userId) {
		String jwtToken = token.replaceFirst("Bearer", "");
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		ResponseDto dto = new ResponseDto();
		if(loggedInUser.isPresent()) {
			Optional<User>user=userRepository.findById(userId);
			List<VideoUpload>list=new ArrayList<>();
			if(loggedInUser.get().getRole().getRole().equals("ADMIN") && user.get().getRole().getRole().equals("ADMIN")) {
				List<VideoUpload>videoUploads=videoUploadRepository.findAll();
				list=videoUploads.stream()
				        .peek(videoUpload -> {
				            videoUpload.setDeleteStatus(true);
				            videoUpload.setEditStatus(true);
				        })
				        .collect(Collectors.toList());
			}else {
				
					List<VideoUpload>videoUploads=videoUploadRepository.findByUser(user.get());
				list=videoUploads.stream()
		        .peek(videoUpload -> {
		            videoUpload.setDeleteStatus(true);
		            videoUpload.setEditStatus(true);
		        })
		        .collect(Collectors.toList());
				
				
			}
			
			
			dto.setVideoUploadsList(list);
			
		return jsonConstractor.responseCreation(true, "get Video List Sucess", null, dto);	
		}
		return jsonConstractor.responseCreation(false, "Loggin User is not present", null, null);
	}

}
