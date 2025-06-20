package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaybeClientDTO {
    private String firstName;
    private String lastName;
    private String email;
}
