package com.ebank.ebanking2.model.dto;


//this is the invoice dto to interact with

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePayDTO {
    private String provider;
    private String referenceNumber;
    private Long clientId;
    private Long compteId;
}
