package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RechargeDTO {

    private String operateur;
    private String phoneNumber;
    private int montant;
    private String rib;

}
