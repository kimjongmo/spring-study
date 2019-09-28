package com.spring.jpa.application;

import com.spring.jpa.domain.Employee;

import java.util.List;


public interface EmployeeListService {

	public List<Employee> getEmployee(String keyword, Long teamId);
}
