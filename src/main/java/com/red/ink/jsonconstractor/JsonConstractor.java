package com.red.ink.jsonconstractor;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.red.ink.dto.ResponseDto;
import com.red.ink.exception.ExceptionBean;
import com.red.ink.model.EventUpload;
import com.red.ink.model.User;
import com.red.ink.response.FilterUtil;
import com.red.ink.response.ResponseModel;

public class JsonConstractor {
	
	 private  ResponseModel response=new ResponseModel() ;	
	 private MappingJacksonValue mappingJacksonValue;

	 public ResponseEntity<Object> responseCreation(Boolean status, String message, String description,
				List<User> users, User user) {
			response.setStatus(status);
			response.setInformation(new ExceptionBean(new Date(), message, description));
			if (users != null) {
				response.setUsers(users);
				mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "users" });
			} else if (user != null) {
				response.setUser(user);
				mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "user" });
			} else {
				mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
			}
			return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
		}

//	public ResponseEntity<Object> responseCreation(boolean status, String message, String description,
//			List<EventUpload> listEventUploads, Object user) {
//		response.setStatus(status);
//		response.setInformation(new ExceptionBean(new Date(), message, description));
//		if(listEventUploads !=null) {
//			response.setListEventUploads(listEventUploads);
//			mappingJacksonValue=FilterUtil.filterFields(response, new String[] {"status","information","listEventUploads"});
//		}else {
//			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
//		}
//		
//		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
//	}
	
	public ResponseEntity<Object> responseCreation(boolean status, String message, String description,
			ResponseDto dto) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if(dto !=null) {
			response.setDto(dto);
			mappingJacksonValue=FilterUtil.filterFields(response, new String[] {"status","information","dto"});
		}else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

	
	 
}
