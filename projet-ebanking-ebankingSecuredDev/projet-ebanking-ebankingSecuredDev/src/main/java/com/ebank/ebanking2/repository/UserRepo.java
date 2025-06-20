package com.ebank.ebanking2.repository;


import com.ebank.ebanking2.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT YEAR(u.createdAt) as year, MONTH(u.createdAt) as month, COUNT(u) as count " +
            "FROM User u WHERE u.createdAt IS NOT NULL GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
            "ORDER BY year, month")
    List<Object[]> countClientsGroupedByYearAndMonth();

    @Query("SELECT YEAR(u.createdAt) as year, COUNT(u) as count " +
            "FROM User u WHERE u.createdAt IS NOT NULL GROUP BY YEAR(u.createdAt) " +
            "ORDER BY year")
    List<Object[]> countClientsGroupedByYear();
}
