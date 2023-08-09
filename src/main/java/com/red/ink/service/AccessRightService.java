/**
 * 
 */
package com.red.ink.service;

import org.springframework.http.ResponseEntity;

import com.red.ink.dto.AcsssRightsDto;

/**
 * @author ajith
 *
 */
public interface AccessRightService {

//public	ResponseEntity<Object> save(AcsssRightsDto acsssRightsDto, String token);

//public ResponseEntity<Object> findRightsByRole(String token, String roleId);

public ResponseEntity<Object> createAccessRights(AcsssRightsDto acsssRightsDto, String token);

public ResponseEntity<Object> getRightsByRole(String token, Long roleId);

	

}
