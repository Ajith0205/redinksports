/**
 * 
 */
package com.red.ink.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author bsoft-ajith
 *
 */

@Entity
@Table(name = "tbl_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//@NotBlank(message = "Enter Your Name")
	private String profile;
	
	private String name;
	private String fatherName;
	private String gender;
	//@NotBlank(message = "enter mailId")
    private String email;
    private String dateofbirth;
	private String placeofBirth;
	private String physicalStatus;
	private String address;
	
	private String aadharNo;
	private String uploadAadhar;
	private String panNo;
	private String uploadPAN;
	private String whatsappNo;
	private String selectRole;
	private String selectgame;
	//@NotBlank(message = "enter a Username")
	private String username;
	//@NotBlank(message = "enter a password")
	private String password;
	private boolean status;
	private String permissions;
//	@NotBlank(message = "upload a profile Photo") 
	
	
	
	//@NotBlank(message = "enter Your Aadhar Number")
	
//	@NotBlank(message = "upload Aadhar photo")
	
//	@NotBlank(message = "enter  your pan Number")
	
	//@NotBlank(message = "upload your pan image")

	//@NotBlank(message = "Enter Your Whats app Number")
	
	


	@ManyToOne
	private Role role;

	@Transient
	private Long roleId;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String name, String gender, String email, String address, String selectRole, String selectgame,
			String username, String password, boolean status, String permissions, String profile, String fatherName,
			String dateofbirth, String placeofBirth, String physicalStatus, String aadharNo, String uploadAadhar,
			String panNo, String uploadPAN, String whatsappNo, Role role, Long roleId) {
		super();
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.address = address;
		this.selectRole = selectRole;
		this.selectgame = selectgame;
		this.username = username;
		this.password = password;
		this.status = status;
		this.permissions = permissions;
		this.profile = profile;
		this.fatherName = fatherName;
		this.dateofbirth = dateofbirth;
		this.placeofBirth = placeofBirth;
		this.physicalStatus = physicalStatus;
		this.aadharNo = aadharNo;
		this.uploadAadhar = uploadAadhar;
		this.panNo = panNo;
		this.uploadPAN = uploadPAN;
		this.whatsappNo = whatsappNo;
		this.role = role;
		this.roleId = roleId;
	}

	public User(String username, String password, String name, String gender, String whatsappNo, String email,
			String address, boolean status, Role role) {
		// TODO Auto-generated constructor stub

		this.name = name;
		this.gender = gender;
		this.whatsappNo = whatsappNo;
		this.email = email;
		this.address = address;
		this.username = username;
		this.password = password;
		this.status = status;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSelectRole() {
		return selectRole;
	}

	public void setSelectRole(String selectRole) {
		this.selectRole = selectRole;
	}

	public String getSelectgame() {
		return selectgame;
	}

	public void setSelectgame(String selectgame) {
		this.selectgame = selectgame;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@ApiModelProperty(hidden = true)
	public List<String> getPermissionList() {
		if (this.permissions.length() > 0) {
			return Arrays.asList(this.permissions.split(","));
		}
		return new ArrayList<>();
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public String getPlaceofBirth() {
		return placeofBirth;
	}

	public void setPlaceofBirth(String placeofBirth) {
		this.placeofBirth = placeofBirth;
	}

	public String getPhysicalStatus() {
		return physicalStatus;
	}

	public void setPhysicalStatus(String physicalStatus) {
		this.physicalStatus = physicalStatus;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getUploadAadhar() {
		return uploadAadhar;
	}

	public void setUploadAadhar(String uploadAadhar) {
		this.uploadAadhar = uploadAadhar;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getUploadPAN() {
		return uploadPAN;
	}

	public void setUploadPAN(String uploadPAN) {
		this.uploadPAN = uploadPAN;
	}

	public String getWhatsappNo() {
		return whatsappNo;
	}

	public void setWhatsappNo(String whatsappNo) {
		this.whatsappNo = whatsappNo;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", gender=" + gender + ", email=" + email + ", address=" + address
				+ ", selectRole=" + selectRole + ", selectgame=" + selectgame + ", username=" + username + ", password="
				+ password + ", status=" + status + ", permissions=" + permissions + ", profile=" + profile
				+ ", fatherName=" + fatherName + ", dateofbirth=" + dateofbirth + ", placeofBirth=" + placeofBirth
				+ ", physicalStatus=" + physicalStatus + ", aadharNo=" + aadharNo + ", uploadAadhar=" + uploadAadhar
				+ ", panNo=" + panNo + ", uploadPAN=" + uploadPAN + ", whatsappNo=" + whatsappNo + ", role=" + role
				+ ", roleId=" + roleId + "]";
	}

	
	

	
	 
}




