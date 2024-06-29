/**
 * 
 */
package com.red.ink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.red.ink.model.User;
import com.red.ink.model.VideoUpload;

/**
 * @author Ajith
 *
 */
public interface VideoUploadRepository extends JpaRepository<VideoUpload,Long> {

	List<VideoUpload> findByUser(User user);

	

}
