package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Compte;
import com.ebank.ebanking2.model.entity.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.data.repository.Repository;

@Repository
public interface RechargeRepo extends JpaRepository<Recharge, Long> {
    Page<Recharge> findByCompte(Compte compte, Pageable pageable);

}
