/**
 * 
 */
package com.red.ink.model;





import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author ajith
 *
 */

@Entity
@Table(name="tbl_accessright")
public class AccessRight {
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
	private boolean status;
	
	@ManyToOne
	private ModuleTbl moduleTbl;
	
	@ManyToMany
	private Set<Feature> feature = new HashSet<Feature>();
	
	//private Feature feature;

    @ManyToOne
	private Role role;

	
	public AccessRight() {
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


	public ModuleTbl getModuleTbl() {
		return moduleTbl;
	}


	public void setModuleTbl(ModuleTbl moduleTbl) {
		this.moduleTbl = moduleTbl;
	}


	public Set<Feature> getFeature() {
		return feature;
	}


	public void setFeature(Set<Feature> feature) {
		this.feature = feature;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	@Override
	public String toString() {
		return "AccessRight [id=" + id + ", status=" + status + ", moduleTbl=" + moduleTbl + ", feature=" + feature
				+ ", role=" + role + "]";
	}





	
}
