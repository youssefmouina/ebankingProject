package com.ebank.ebanking2.model.dto;

import com.ebank.ebanking2.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Role role;
}
