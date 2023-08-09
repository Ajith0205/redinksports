/**
 * 
 */
package com.red.ink.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;





/**
 * @author ajith
 *
 */
@Entity
@Table(name="tbl_feature")
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long id;

	private String featureName;

	private String description;

	private boolean status;
    
	@ManyToOne(targetEntity=ModuleTbl.class)
	private ModuleTbl moduleTbl;

	
public Feature() {
	// TODO Auto-generated constructor stub
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getFeatureName() {
	return featureName;
}

public void setFeatureName(String featureName) {
	this.featureName = featureName;
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

public ModuleTbl getModuleTbl() {
	return moduleTbl;
}

public void setModuleTbl(ModuleTbl moduleTbl) {
	this.moduleTbl = moduleTbl;
}

@Override
public String toString() {
	return "Feature [id=" + id + ", featureName=" + featureName + ", description=" + description + ", status=" + status
			+ ", moduleTbl=" + moduleTbl + "]";
}




	
}
