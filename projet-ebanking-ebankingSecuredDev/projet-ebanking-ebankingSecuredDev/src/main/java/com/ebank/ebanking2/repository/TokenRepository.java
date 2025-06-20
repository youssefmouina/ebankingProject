package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllByUserId(Long userId);

    Token findByToken(String token);

    List<Token> findByUserIdAndExpiredFalseAndRevokedFalse(Long userId);

    @Query("SELECT t FROM Token t WHERE t.userId = :userId AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokensByUserId(String userId);
}
