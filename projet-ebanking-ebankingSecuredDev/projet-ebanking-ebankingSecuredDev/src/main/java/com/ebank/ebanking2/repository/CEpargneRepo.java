package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.CEpargne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CEpargneRepo extends JpaRepository<CEpargne, Long> {
    long count();
    List<CEpargne> findByCreatedAtAfter(LocalDateTime after);

    @Query("SELECT COALESCE(AVG(c.solde), 0) FROM CEpargne c")
    double calculateAverageDeposit();

    @Query("SELECT COUNT(c) FROM CEpargne c WHERE YEAR(c.createdAt) = YEAR(CURRENT_DATE) AND MONTH(c.createdAt) = MONTH(CURRENT_DATE)")
    int countNewAccountsThisMonth();

    @Query("SELECT COUNT(c) FROM CEpargne c WHERE YEAR(c.createdAt) = YEAR(CURRENT_DATE) AND MONTH(c.createdAt) = MONTH(CURRENT_DATE)")
    int countCurrentMonthAccounts();

    @Query("SELECT COUNT(c) FROM CEpargne c WHERE YEAR(c.createdAt) = YEAR(CURRENT_DATE) AND MONTH(c.createdAt) = MONTH(CURRENT_DATE) - 1")
    int countPreviousMonthAccounts();

}
