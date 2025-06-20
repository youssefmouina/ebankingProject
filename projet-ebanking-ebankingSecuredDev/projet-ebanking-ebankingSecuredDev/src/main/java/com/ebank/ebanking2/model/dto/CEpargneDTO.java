package com.ebank.ebanking2.model.dto;

import com.ebank.ebanking2.model.entity.StatusCompte;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CEpargneDTO {
    private Long clientId;
    private double solde;
    private StatusCompte status;
    private Double tauxInterets;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateInterets;
}
