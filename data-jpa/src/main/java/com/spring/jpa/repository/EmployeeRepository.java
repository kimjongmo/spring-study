package com.spring.jpa.repository;


import com.spring.jpa.domain.Employee;
import org.springframework.data.repository.Repository;

public interface EmployeeRepository extends Repository<Employee, Long> {
    public Employee save(Employee emp);
    public Employee findOne(Long id);
}
