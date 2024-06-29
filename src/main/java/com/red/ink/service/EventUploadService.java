package com.red.ink.service;

import org.springframework.http.ResponseEntity;

import com.red.ink.model.EventUpload;

public interface EventUploadService {

	

public	ResponseEntity<Object> eventSave(EventUpload eventUpload, String token);

public ResponseEntity<Object> eventLists( String token);

public ResponseEntity<Object> eventUpdate(EventUpload eventUpload, String token);

public ResponseEntity<Object> eventDelete(String token, Long id);

public ResponseEntity<Object> finById(Long id, String token);

public ResponseEntity<Object> removeEventImage(Long eventId, String token);

public ResponseEntity<Object> getEventsUserBased(String token, Long userId);

}
