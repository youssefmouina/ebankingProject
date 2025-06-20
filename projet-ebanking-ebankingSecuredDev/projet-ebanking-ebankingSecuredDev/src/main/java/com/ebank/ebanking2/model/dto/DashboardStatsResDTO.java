package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResDTO {
    private Long totalClients;
    private Long totalAccountCurrent;
    private Long totalAccountSavings;
}
