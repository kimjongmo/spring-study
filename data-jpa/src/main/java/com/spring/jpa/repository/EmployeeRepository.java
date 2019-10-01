package com.spring.jpa.repository;


import com.spring.jpa.domain.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, Long> {
    Employee save(Employee emp);

    Employee findById(Long id);

    List<Employee> findAll(Specification<Employee> spec);
}
