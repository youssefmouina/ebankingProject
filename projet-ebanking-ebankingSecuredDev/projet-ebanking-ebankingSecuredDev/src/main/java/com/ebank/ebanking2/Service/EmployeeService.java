package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.EmployeeDTO;
import com.ebank.ebanking2.model.dto.EmployeeResDTO;
import com.ebank.ebanking2.model.entity.Employee;
import com.ebank.ebanking2.model.entity.Role;
import com.ebank.ebanking2.model.mapper.ClientMapper;
import com.ebank.ebanking2.model.mapper.EmployeeMapper;
import com.ebank.ebanking2.repository.ClientRepo;
import com.ebank.ebanking2.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public EmployeeResDTO saveEmp(EmployeeDTO employeeDTO) {
        employeeDTO.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        Employee e=this.employeeRepo.save(employeeMapper.toEntity(employeeDTO));
        return this.employeeMapper.toResDTO(e);
    }
    public Page<EmployeeResDTO> getAllEmp(int offset, int size) {
        Pageable pageable = PageRequest.of(offset, size);
        Page<Employee> employees = this.employeeRepo.findAll(pageable);
        return employees.map(employeeMapper::toResDTO);
    }
    public EmployeeResDTO getEmpById(Long id) {
        Employee employee = this.employeeRepo.findById(id).orElseThrow();
        return employeeMapper.toResDTO(employee);
    }
    public boolean isSuperAdmin(Long id) {
        try {
            EmployeeResDTO employee = getEmpById(id);
            return employee != null && employee.getRole() == Role.SUPER_ADMIN;
        } catch (Exception e) {
            return false;
        }
    }


}
