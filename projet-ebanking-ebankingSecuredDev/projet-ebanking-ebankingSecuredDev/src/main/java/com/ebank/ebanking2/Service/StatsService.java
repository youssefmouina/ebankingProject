package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.BalanceDistributionDTO;
import com.ebank.ebanking2.model.dto.CurrentAccountsSummaryStatsDto;
import com.ebank.ebanking2.model.dto.DashboardStatsResDTO;
import com.ebank.ebanking2.model.dto.SavingsAccountSummaryStatsDTO;
import com.ebank.ebanking2.model.entity.CEpargne;
import com.ebank.ebanking2.repository.CCourantRepo;
import com.ebank.ebanking2.repository.CEpargneRepo;
import com.ebank.ebanking2.repository.ClientRepo;
import com.ebank.ebanking2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    CCourantRepo cCourantRepo;
    @Autowired
    CEpargneRepo cEpargneRepo;
    @Autowired
    UserRepo userRepo;
    public DashboardStatsResDTO getStatsForDash(){
        DashboardStatsResDTO dashboardStatsResDTO = new DashboardStatsResDTO();
        dashboardStatsResDTO.setTotalClients(clientRepo.count());
        dashboardStatsResDTO.setTotalAccountCurrent(cCourantRepo.count());
        dashboardStatsResDTO.setTotalAccountSavings(cEpargneRepo.count());
        return dashboardStatsResDTO;
    }
    public Map<String, Long> getClientInscriptionsByMonth() {
        List<Object[]> results = userRepo.countClientsGroupedByYearAndMonth();
        Map<String, Long> map = new LinkedHashMap<>();

        for (Object[] row : results) {
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            Long count = (Long) row[2];
            String key = String.format("%04d-%02d", year, month); // e.g. "2024-06"
            map.put(key, count);
        }
        return map;
    }

    public Map<Integer, Long> getClientInscriptionsByYear() {
        List<Object[]> results = userRepo.countClientsGroupedByYear();
        Map<Integer, Long> map = new LinkedHashMap<>();

        for (Object[] row : results) {
            Integer year = (Integer) row[0];
            Long count = (Long) row[1];
            map.put(year, count);
        }
        return map;
    }


        // Filter + limit for monthly stats
        public Map<String, Integer> getMonthlyAccountOpenings(String filter, int limit) {
            Map<YearMonth, Integer> rawData = getMonthlyAccountOpeningsRaw();

            int maxMonths = switch (filter) {
                case "quarter" -> 3;
                case "halfYear" -> 6;
                default -> 6;
            };

            int effectiveLimit = Math.min(limit, maxMonths);

            List<Map.Entry<YearMonth, Integer>> entries = new ArrayList<>(rawData.entrySet());
            int startIndex = Math.max(entries.size() - maxMonths, 0);
            List<Map.Entry<YearMonth, Integer>> filteredEntries = entries.subList(startIndex, entries.size());

            int sliceStart = Math.max(filteredEntries.size() - effectiveLimit, 0);
            List<Map.Entry<YearMonth, Integer>> limitedEntries = filteredEntries.subList(sliceStart, filteredEntries.size());

            Map<String, Integer> result = new LinkedHashMap<>();
            for (var entry : limitedEntries) {
                String monthLabel = capitalizeFirstLetter(entry.getKey().getMonth().name().substring(0,3).toLowerCase());
                result.put(monthLabel, entry.getValue());
            }

            return result;
        }

        // Filter + limit for yearly stats
        public Map<String, Integer> getYearlyAccountOpenings(String filter, int limit) {
            Map<Integer, Integer> rawData = getYearlyAccountOpeningsRaw();

            int maxYears = 6;

            int effectiveLimit = Math.min(limit, maxYears);

            List<Integer> years = new ArrayList<>(rawData.keySet());
            Collections.sort(years);

            int startIndex = Math.max(years.size() - maxYears, 0);
            List<Integer> filteredYears = years.subList(startIndex, years.size());

            int sliceStart = Math.max(filteredYears.size() - effectiveLimit, 0);
            List<Integer> limitedYears = filteredYears.subList(sliceStart, filteredYears.size());

            Map<String, Integer> result = new LinkedHashMap<>();
            for (Integer year : limitedYears) {
                result.put(year.toString(), rawData.get(year));
            }

            return result;
        }

        private String capitalizeFirstLetter(String input) {
            if (input == null || input.isEmpty()) return input;
            return input.substring(0,1).toUpperCase() + input.substring(1);
        }
    public Map<YearMonth, Integer> getMonthlyAccountOpeningsRaw() {
        List<Object[]> results = cCourantRepo.countClientAccountsGroupedByYearMonth();
        Map<YearMonth, Integer> rawData = new LinkedHashMap<>();

        for (Object[] row : results) {
            int year = (int) row[0];
            int month = (int) row[1];
            long count = (long) row[2];
            rawData.put(YearMonth.of(year, month), (int) count);
        }

        return rawData;
    }

    public Map<Integer, Integer> getYearlyAccountOpeningsRaw() {
        List<Object[]> results = cCourantRepo.countClientAccountsGroupedByYear();
        Map<Integer, Integer> rawData = new LinkedHashMap<>();

        for (Object[] row : results) {
            int year = (int) row[0];
            long count = (long) row[1];
            rawData.put(year, (int) count);
        }

        return rawData;
    }
    public BalanceDistributionDTO getBalanceDistribution() {
        List<Object[]> result = cCourantRepo.getBalanceDistribution();
        BalanceDistributionDTO dto = new BalanceDistributionDTO();

        for (Object[] row : result) {
            String range = (String) row[0];
            Long count = (Long) row[1];

            switch (range) {
                case "<1k" -> dto.setLessThan1k(count);
                case "1k–10k" -> dto.setBetween1kAnd10k(count);
                case "10k–50k" -> dto.setBetween10kAnd50k(count);
                case ">50k" -> dto.setGreaterThan50k(count);
            }
        }

        return dto;
    }
    public CurrentAccountsSummaryStatsDto getAccountSummaryStats() {
        long totalAccounts = cCourantRepo.count();

        // Example queries - adapt to your schema
        Double totalBalance = cCourantRepo.sumBalance(); // custom @Query for SUM(balance)
        if (totalBalance == null) totalBalance = 0.0;

        Double averageBalance = cCourantRepo.averageBalance();
        if (averageBalance == null) averageBalance = 0.0;

        // Count accounts created this month
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDay = now.withDayOfMonth(1);
        long newThisMonth = cCourantRepo.countByCreatedAtAfter(firstDay);

        return new CurrentAccountsSummaryStatsDto(totalAccounts, totalBalance, averageBalance, newThisMonth);
    }

    public Map<String, Long> getMonthlyStats(int lastN) {
        LocalDateTime start = LocalDateTime.now().minusMonths(lastN - 1).withDayOfMonth(1);
        List<CEpargne> accounts = cEpargneRepo.findByCreatedAtAfter(start);

        return accounts.stream()
                .collect(Collectors.groupingBy(
                        acc -> acc.getCreatedAt().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getYearlyStats(int lastN) {
        int currentYear = LocalDate.now().getYear();
        List<CEpargne> accounts = cEpargneRepo.findByCreatedAtAfter(LocalDateTime.of(currentYear - lastN + 1, 1, 1, 0, 0));

        return accounts.stream()
                .collect(Collectors.groupingBy(
                        acc -> String.valueOf(acc.getCreatedAt().getYear()),
                        TreeMap::new,
                        Collectors.counting()
                ));
    }
    public SavingsAccountSummaryStatsDTO getSavingsSummaryStats() {
        double average = cEpargneRepo.calculateAverageDeposit();
        int current = cEpargneRepo.countCurrentMonthAccounts();
        int previous = cEpargneRepo.countPreviousMonthAccounts();

        double growth = (previous == 0) ? 0 : ((double)(current - previous) / previous) * 100;

        int newAccounts = cEpargneRepo.countNewAccountsThisMonth();

        return new SavingsAccountSummaryStatsDTO(average, growth, newAccounts);
    }




}
