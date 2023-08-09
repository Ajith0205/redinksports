/**
 * 
 */
package com.red.ink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.red.ink.dto.AcsssRightsDto;
import com.red.ink.model.AccessRight;
import com.red.ink.service.AccessRightService;

/**
 * @author ajith
 *
 */

@RestController
@RequestMapping("/accessRight/")
public class AccessRightController {
	
@Autowired
private	AccessRightService accessRightService;

@PostMapping("save")
public ResponseEntity<Object> saveAccessRights(@RequestBody AcsssRightsDto acsssRightsDto, 
		@RequestHeader("Authorization") String token) {
	return accessRightService.createAccessRights(acsssRightsDto, token);
}
@GetMapping("findRightsByRole")
public ResponseEntity<Object> findRightsByRole( @RequestParam ("roleId") Long roleId, 
		@RequestHeader("Authorization") String token) {
	return accessRightService.getRightsByRole(token, roleId);
}

	
	
}
