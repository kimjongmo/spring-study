package com.spring.hrdb.app.service;

import com.spring.hrdb.app.dao.EmployeeDao;
import com.spring.hrdb.app.model.Employee;
import org.springframework.transaction.annotation.Transactional;


public class EmployeeRegistryServiceImpl implements EmployeeRegistryService {

	private EmployeeDao empDao;

	public void setEmpDao(EmployeeDao empDao) {
		this.empDao = empDao;
	}

	@Transactional
	@Override
	public Long register(Employee emp) {
		Employee oldEmp = empDao.selectByEmployeeNumber(emp.getNumber());
		if (oldEmp != null)
			throw new DuplicateEmpNumberException();
		return empDao.insert(emp);
	}

}
