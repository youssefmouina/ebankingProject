package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Page<Employee> findAll(Pageable pageable);
}
