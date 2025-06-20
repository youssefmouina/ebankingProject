package com.ebank.ebanking2.model.mapper;

import com.ebank.ebanking2.model.dto.EmployeeDTO;
import com.ebank.ebanking2.model.dto.EmployeeResDTO;
import com.ebank.ebanking2.model.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    public Employee toEntity(EmployeeDTO employeeDTO);
    public EmployeeResDTO toResDTO(Employee employee);
}
