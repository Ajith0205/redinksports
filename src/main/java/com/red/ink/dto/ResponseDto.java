package com.red.ink.dto;

import java.util.List;

import com.red.ink.model.EventUpload;
import com.red.ink.model.VideoUpload;

public class ResponseDto {
	
	private List<EventUpload> eventList;
	private EventUpload event;
	
	private AcsssRightsDto accessRightsDto;
	
	private String templatePath;
	
	private String errorMessage;
	private List<VideoUpload>videoUploadsList;
	
	
	public List<VideoUpload> getVideoUploadsList() {
		return videoUploadsList;
	}

	public void setVideoUploadsList(List<VideoUpload> videoUploadsList) {
		this.videoUploadsList = videoUploadsList;
	}

	public AcsssRightsDto getAccessRightsDto() {
		return accessRightsDto;
	}

	public void setAccessRightsDto(AcsssRightsDto accessRightsDto) {
		this.accessRightsDto = accessRightsDto;
	}

	public ResponseDto() {
		// TODO Auto-generated constructor stub
	}

	public List<EventUpload> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventUpload> eventList) {
		this.eventList = eventList;
	}

	public EventUpload getEvent() {
		return event;
	}

	public void setEvent(EventUpload event) {
		this.event = event;
	}

       
	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ResponseDto [eventList=" + eventList + ", event=" + event + ", accessRightsDto=" + accessRightsDto
				+ ", templatePath=" + templatePath + ", errorMessage=" + errorMessage + ", videoUploadsList="
				+ videoUploadsList + "]";
	}


}
