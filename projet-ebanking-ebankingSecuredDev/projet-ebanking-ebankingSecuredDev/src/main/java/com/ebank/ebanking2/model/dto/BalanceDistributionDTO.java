package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceDistributionDTO {
    private long lessThan1k;
    private long between1kAnd10k;
    private long between10kAnd50k;
    private long greaterThan50k;

    // Getters and setters
}
