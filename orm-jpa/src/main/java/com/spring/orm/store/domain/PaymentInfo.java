package com.spring.orm.store.domain;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT_INFO")
public class PaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAYMENT_INFO_ID")
	private Integer id;
	
	@Column(name = "PRICE")
	private int price;

	public PaymentInfo() {
	}

	public PaymentInfo(int price) {
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
