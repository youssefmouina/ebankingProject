package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClientDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String job;
    private String password;
    private boolean valid;
}