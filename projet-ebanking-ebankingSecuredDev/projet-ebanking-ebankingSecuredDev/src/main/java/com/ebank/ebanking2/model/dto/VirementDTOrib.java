package com.ebank.ebanking2.model.dto;

import com.ebank.ebanking2.model.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirementDTOrib {
    private String compteEmetteur;
    private String compteRecepteur;
    private double montant;
    private Type type;
}