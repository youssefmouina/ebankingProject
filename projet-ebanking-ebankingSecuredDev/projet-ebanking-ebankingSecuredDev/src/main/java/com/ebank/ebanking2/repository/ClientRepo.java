package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> getFirstByOrderByIdDesc();

    Optional<Client> findByEmail(String email);
    Optional<Client> findByEmailAndIsRecoveryPasswordTokenVerified(String email, boolean status);
    Optional<Client> findByEmailAndIsRecoveryPasswordTokenVerifiedAndRecoveryPasswordTokenAndRecoveryPasswordTokenExpirationTimeAfter(String email, boolean status, String recoveryToken, LocalDateTime recoveryPasswordTokenExpirationTimeAfter);

    Optional<User> findByEmailAndEmail(String email,String email1);
    long count();
}
