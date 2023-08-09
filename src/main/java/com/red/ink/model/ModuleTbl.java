/**
 * 
 */
package com.red.ink.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;





/**
 * @author ajith
 *
 */

@Entity
@Table(name="tbl_module")
public class ModuleTbl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String moduleName;

	private String description;


	private boolean status;

	public ModuleTbl() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "ModuleTbl [id=" + id + ", moduleName=" + moduleName + ", description=" + description + ", status="
				+ status + "]";
	}




}
