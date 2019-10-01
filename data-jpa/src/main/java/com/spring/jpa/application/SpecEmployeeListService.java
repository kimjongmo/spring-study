package com.spring.jpa.application;


import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;


import com.spring.jpa.domain.Employee;
import com.spring.jpa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import static com.spring.jpa.domain.EmployeeSpec.*;
import static org.springframework.data.jpa.domain.Specification.where;

public class SpecEmployeeListService implements EmployeeListService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Transactional
	@Override
	public List<Employee> getEmployee(String keyword, Long teamId) {
		if (hasValue(keyword) || hasValue(teamId)) {
			if (hasValue(keyword) && !hasValue(teamId)) {
				return employeeRepository.findAll(
						where(nameEq(keyword)).or(employeeNumberEq(keyword))
				);
			} else if (!hasValue(keyword) && hasValue(teamId)) {
				return employeeRepository.findAll(teamIdEq(teamId));
			} else {
				Specification<Employee> spec1 = where(nameEq(keyword)).or(employeeNumberEq(keyword));
				return employeeRepository.findAll(spec1.and(teamIdEq(teamId)));
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -30);
			return employeeRepository.findAll(joinedDateGt(cal.getTime()));
		}
	}

	private boolean hasValue(Object value) {
		return value != null;
	}

	private boolean hasValue(String value) {
		return value != null && !value.trim().isEmpty();
	}
}
