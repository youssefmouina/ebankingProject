package com.ebank.ebanking2.model.dto;


import com.ebank.ebanking2.model.entity.StatusCompte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CompteResDTOChat {

    private Long id;
    private String rib;
    private String accountType;
}
