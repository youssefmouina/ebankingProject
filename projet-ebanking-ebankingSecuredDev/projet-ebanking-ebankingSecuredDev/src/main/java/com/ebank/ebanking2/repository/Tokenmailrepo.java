package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.tokenmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Tokenmailrepo extends JpaRepository<tokenmail, Long> {
    tokenmail findByClient(Client clientId);

    Optional<tokenmail> findByTokenAndClient(String token, Client client);
}
