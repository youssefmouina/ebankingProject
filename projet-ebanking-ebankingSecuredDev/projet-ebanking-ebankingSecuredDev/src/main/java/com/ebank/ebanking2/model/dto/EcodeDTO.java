package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcodeDTO {

    private Long clientId;
    private String code;
}
