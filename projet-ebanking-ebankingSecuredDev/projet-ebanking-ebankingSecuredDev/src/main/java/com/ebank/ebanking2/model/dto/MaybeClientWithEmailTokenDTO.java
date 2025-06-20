package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaybeClientWithEmailTokenDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String emailToken;
}
