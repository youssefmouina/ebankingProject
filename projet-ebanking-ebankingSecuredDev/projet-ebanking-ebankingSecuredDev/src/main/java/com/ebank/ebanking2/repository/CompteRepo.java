package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.CCourant;
import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.Compte;
import com.ebank.ebanking2.model.entity.StatusCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import jakarta.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface CompteRepo extends JpaRepository<Compte, Long> {
    List<Compte> findByClientId(Long clientId);
    Optional<Compte> getFirstByOrderByIdDesc();



    //@Query("{ 'clientId': ?0, 'status': ?1, '_class': ?2 }")
    List<CCourant> findByClientIdAndStatusAndClass(Long clientId, StatusCompte statusCompte, String className);
    Optional<CCourant> findByRib(String rib);
    //@Query("SELECT c.solde FROM Compte c WHERE c.rib = rib")
    double findSoldeByRib(@Param("rib") String rib);


    List<Compte> findByClientIdAndStatus(Long clientId, StatusCompte statusCompte);


}

