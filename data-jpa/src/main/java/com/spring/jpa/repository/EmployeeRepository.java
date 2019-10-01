package com.spring.jpa.repository;


import com.spring.jpa.domain.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, Long> {
    public Employee save(Employee emp);
    public Employee findOne(Long id);
    public List<Employee> findAll(Specification<Employee> spec);
}
