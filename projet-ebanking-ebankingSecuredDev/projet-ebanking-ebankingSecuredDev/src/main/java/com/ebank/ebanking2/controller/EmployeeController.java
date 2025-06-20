package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.EmployeeService;
import com.ebank.ebanking2.model.dto.EmployeeDTO;
import com.ebank.ebanking2.model.dto.EmployeeResDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE') and @employeeService.isSuperAdmin(authentication.principal.id)")
    public ResponseEntity<Page<EmployeeResDTO>> getEmployees(@RequestParam("offset") int offset,@RequestParam("size") int size) {
        Page<EmployeeResDTO> employees = employeeService.getAllEmp( offset,  size);
        return ResponseEntity.ok(employees);
    }
    @PreAuthorize("hasRole('EMPLOYEE') and @employeeService.isSuperAdmin(authentication.principal.id)")
    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeResDTO> getEmployee(@PathVariable("id") Long id) {
        EmployeeResDTO employee = employeeService.getEmpById(id);
        return ResponseEntity.ok(employee);
    }
    @PostMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE') and @employeeService.isSuperAdmin(authentication.principal.id)")
    public ResponseEntity<EmployeeResDTO> saveEmployee(@RequestBody EmployeeDTO employee) {
        return ResponseEntity.ok(employeeService.saveEmp(employee));
    }
    @PostMapping
//    @PreAuthorize("hasRole('EMPLOYEE') and @employeeService.isSuperAdmin(authentication.principal.id)")
    public ResponseEntity<List<EmployeeResDTO>> saveEmployees(@RequestBody List<EmployeeDTO> employeesDTO) {
        List<EmployeeResDTO> employees=new ArrayList<>();
        employeesDTO.forEach(employee -> employees.add(employeeService.saveEmp(employee)));
        return ResponseEntity.ok(employees);
    }



}
