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
@Table(name="tbl_payment")
public class FessPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private double price;
	private String currency;
	private String method;
	private String intent;
	private String description;
	
	public FessPayment() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "FessPayment [id=" + id + ", price=" + price + ", currency=" + currency + ", method=" + method
				+ ", intent=" + intent + ", description=" + description + "]";
	}
	

}
