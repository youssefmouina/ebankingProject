package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcheteDto {
    private String name;
    private double montant;
    private double actuealprisecurrency;

}
