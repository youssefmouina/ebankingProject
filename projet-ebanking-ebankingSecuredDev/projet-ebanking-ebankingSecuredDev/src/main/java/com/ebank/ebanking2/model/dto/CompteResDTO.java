package com.ebank.ebanking2.model.dto;

import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.Recharge;
import com.ebank.ebanking2.model.entity.StatusCompte;
import com.ebank.ebanking2.model.entity.Virement;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class CompteResDTO {
    private Long id;
    private String rib;
    private double solde;
    private StatusCompte status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    private double tauxInterets;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateInterets;
    private boolean autorisePaiementEnLigne;
    private List<VirementResDTO> virementsEmis;
    private List<VirementResDTO> virementsRecu;
    private List<RechargeResDTO> recharges;
    private String accountType;
}
