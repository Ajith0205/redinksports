/**
 * 
 */
package com.red.ink.dto;

import java.util.List;

import javax.persistence.ManyToMany;

/**
 * @author ajith
 *
 */
public class ModuleTblDto {

	private Long id;
	private String name;
	
	private boolean status;
	
	@ManyToMany
	private List<FeatureDto>featureDto;
	
	public ModuleTblDto() {
		// TODO Auto-generated constructor stub
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


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public List<FeatureDto> getFeatureDto() {
		return featureDto;
	}


	public void setFeatureDto(List<FeatureDto> featureDto) {
		this.featureDto = featureDto;
	}


	@Override
	public String toString() {
		return "ModuleTblDto [id=" + id + ", name=" + name + ", status=" + status + ", featureDto=" + featureDto + "]";
	}


	
	
	
}
