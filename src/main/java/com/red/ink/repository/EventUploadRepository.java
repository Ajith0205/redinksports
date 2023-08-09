/**
 * 
 */
package com.red.ink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.red.ink.model.EventUpload;

/**
 * @author ajith
 *
 */
public interface EventUploadRepository extends JpaRepository<EventUpload, Long> {
	//userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
	//@Query(value = "SELECT u FROM EventUpload u ORDER BY id")
	@Query(value = "SELECT c from EventUpload c order by c.eventDate asc")
	List<EventUpload> findAll();
	
}
