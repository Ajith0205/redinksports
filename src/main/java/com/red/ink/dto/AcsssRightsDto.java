/**
 * 
 */
package com.red.ink.dto;

import java.util.List;

import javax.persistence.ManyToMany;

import com.red.ink.model.ModuleTbl;

/**
 * @author ajith
 *
 */
public class AcsssRightsDto {
	
	private Long id;
	private boolean status;
	private Long roleId;
	private String roleName;
	
	@ManyToMany
	private List<ModuleTblDto>moduleTblDto;
	
	public AcsssRightsDto() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<ModuleTblDto> getModuleTblDto() {
		return moduleTblDto;
	}

	public void setModuleTblDto(List<ModuleTblDto> moduleTblDto) {
		this.moduleTblDto = moduleTblDto;
	}

	@Override
	public String toString() {
		return "AcsssRightsDto [id=" + id + ", status=" + status + ", roleId=" + roleId + ", roleName=" + roleName
				+ ", moduleTblDto=" + moduleTblDto + "]";
	}

	

}
