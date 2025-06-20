package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.CCourant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CCourantRepo extends JpaRepository<CCourant, Long>{
    long count();
    @Query("SELECT YEAR(c.createdAt) as yr, MONTH(c.createdAt) as mon, COUNT(c) " +
            "FROM CCourant c JOIN c.client cl " +
            "WHERE TYPE(cl) = Client " +
            "GROUP BY YEAR(c.createdAt), MONTH(c.createdAt) " +
            "ORDER BY yr, mon")
    List<Object[]> countClientAccountsGroupedByYearMonth();

    @Query("SELECT YEAR(c.createdAt) as yr, COUNT(c) " +
            "FROM CCourant c JOIN c.client cl " +
            "WHERE TYPE(cl) = Client " +
            "GROUP BY YEAR(c.createdAt) " +
            "ORDER BY yr")
    List<Object[]> countClientAccountsGroupedByYear();
    @Query("""
    SELECT 
        CASE 
            WHEN c.solde < 1000 THEN '<1k'
            WHEN c.solde >= 1000 AND c.solde < 10000 THEN '1k–10k'
            WHEN c.solde >= 10000 AND c.solde < 50000 THEN '10k–50k'
            ELSE '>50k'
        END AS rangeLabel,
        COUNT(c)
    FROM CCourant c
    GROUP BY rangeLabel
""")
    List<Object[]> getBalanceDistribution();

    @Query("SELECT SUM(a.solde) FROM CCourant a")
    Double sumBalance();

    @Query("SELECT AVG(a.solde) FROM CCourant a")
    Double averageBalance();

    long countByCreatedAtAfter(LocalDateTime date);

}


