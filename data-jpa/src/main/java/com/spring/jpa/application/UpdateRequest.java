package com.spring.jpa.application;


import com.spring.jpa.domain.Address;

public class UpdateRequest {

	private Long employeeId;
	private Address newAddress;

	public UpdateRequest(Long employeeId, Address newAddress) {
		this.employeeId = employeeId;
		this.newAddress = newAddress;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Address getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(Address newAddress) {
		this.newAddress = newAddress;
	}

}
