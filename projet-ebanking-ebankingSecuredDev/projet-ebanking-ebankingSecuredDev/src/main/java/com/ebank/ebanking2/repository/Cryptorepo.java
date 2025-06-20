package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.dto.Cryptodtoreqacheter;
import com.ebank.ebanking2.model.entity.CCourant;
import com.ebank.ebanking2.model.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Cryptorepo extends JpaRepository<Crypto, Long> {
     Crypto findByCcourant(CCourant ccourant);
     Crypto findByCcourantId(Long id);
}
