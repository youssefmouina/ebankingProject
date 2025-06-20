package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsAccountSummaryStatsDTO {
    private double averageDeposit;
    private double growthRate;
    private int newAccounts;
}