package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Banque;
import jakarta.data.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BanqueRepo extends JpaRepository<Banque, Long> {
}
