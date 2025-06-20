package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.MaybeClient;
import jakarta.data.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaybeClientRepo extends JpaRepository<MaybeClient, Long> {
    List<MaybeClient> findByEmailOrderByCreatedAtAsc(String email);
    List<MaybeClient> findByEmailAndEmailTokenExpirationTimeAfterOrderByCreatedAtAsc(String email, LocalDateTime tokenExpirationTime);
}
