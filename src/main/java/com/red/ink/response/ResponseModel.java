package com.red.ink.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.red.ink.dto.ResponseDto;
import com.red.ink.exception.ExceptionBean;
import com.red.ink.model.EventUpload;
import com.red.ink.model.Role;
import com.red.ink.model.User;


@JsonFilter("ResponseModelFilter")
public class ResponseModel {
	
	private boolean status;

	private ExceptionBean information;

	private ResponseDto dto;
	
	private Role role;

	private List<Role> roles;

	private List<User> users;

	private User user;
	
	private List<EventUpload> listEventUploads;
	
	private EventUpload eventUpload;
	
	

	public EventUpload getEventUpload() {
		return eventUpload;
	}

	public void setEventUpload(EventUpload eventUpload) {
		this.eventUpload = eventUpload;
	}

	public List<EventUpload> getEventUploads() {
		return listEventUploads;
	}

	public void setEventUploads(List<EventUpload> eventUploads) {
		this.listEventUploads = eventUploads;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ExceptionBean getInformation() {
		return information;
	}

	public void setInformation(ExceptionBean information) {
		this.information = information;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	public ResponseDto getDto() {
		return dto;
	}

	public void setDto(ResponseDto dto) {
		this.dto = dto;
	}

	@Override
	public String toString() {
		return "ResponseModel [status=" + status + ", information=" + information + ", dto=" + dto + ", role=" + role
				+ ", roles=" + roles + ", users=" + users + ", user=" + user + ", listEventUploads=" + listEventUploads
				+ ", eventUpload=" + eventUpload + "]";
	}
	
	
	

}
