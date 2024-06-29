package com.red.ink.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="tbl_event")

public class EventUpload {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private  String image;
	private String eventName;
	private String eventDate;
	private String eventPlace;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
	
	@Transient
	private boolean deleteStatus;
	
	@Transient
	private boolean editstatus;
	
	public EventUpload() {
		// TODO Auto-generated constructor stub
	}
	

	public EventUpload(String image, String eventName, String eventDate, String eventPlace) {
		super();
		this.image = image;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventPlace = eventPlace;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getEventDate() {
		return eventDate;
	}


	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	public boolean isDeleteStatus() {
		return deleteStatus;
	}


	public void setDeleteStatus(boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}


	public boolean isEditstatus() {
		return editstatus;
	}


	public void setEditstatus(boolean editstatus) {
		this.editstatus = editstatus;
	}


	@Override
	public String toString() {
		return "EventUpload [id=" + id + ", image=" + image + ", eventName=" + eventName + ", eventDate=" + eventDate
				+ ", eventPlace=" + eventPlace + ", user=" + user + ", deleteStatus=" + deleteStatus + ", editstatus="
				+ editstatus + "]";
	}


	

}
