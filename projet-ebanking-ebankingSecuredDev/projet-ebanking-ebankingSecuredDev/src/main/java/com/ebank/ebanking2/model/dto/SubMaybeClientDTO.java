package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubMaybeClientDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
}
