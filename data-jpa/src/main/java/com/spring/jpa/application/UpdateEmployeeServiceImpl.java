package com.spring.jpa.application;

import com.spring.jpa.domain.Employee;
import com.spring.jpa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class UpdateEmployeeServiceImpl implements UpdateEmployeeService {

	@Autowired
	private EmployeeRepository emploeeRepository;

	@Transactional
	@Override
	public void updateEmployee(UpdateRequest updateReq) {
		Employee employee = emploeeRepository.findOne(updateReq.getEmployeeId());
		if (employee == null)
			throw new EmployeeNotFoundException();

		employee.setAddress(updateReq.getNewAddress());
	}

}
