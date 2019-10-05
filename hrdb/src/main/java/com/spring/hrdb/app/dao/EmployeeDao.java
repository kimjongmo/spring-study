package com.spring.hrdb.app.dao;

import com.spring.hrdb.app.model.Employee;

import java.util.List;


public interface EmployeeDao {
	public Long insert(Employee emp);

	public Employee selectOne(Long id);

	public List<Employee> selectList(SearchCondition cond);

	public Employee selectByEmployeeNumber(String number);
}
