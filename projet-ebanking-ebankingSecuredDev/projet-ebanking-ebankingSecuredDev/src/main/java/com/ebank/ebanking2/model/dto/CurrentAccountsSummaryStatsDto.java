package com.ebank.ebanking2.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentAccountsSummaryStatsDto {
    private long totalAccounts;
    private double totalBalance;
    private double averageBalance;
    private long newThisMonth;
}