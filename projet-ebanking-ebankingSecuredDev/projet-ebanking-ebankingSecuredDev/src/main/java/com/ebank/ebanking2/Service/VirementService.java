package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.*;
import com.ebank.ebanking2.model.mapper.VirementMapper;
import com.ebank.ebanking2.repository.ClientRepo;
import com.ebank.ebanking2.repository.CompteRepo;
import com.ebank.ebanking2.repository.VirementRepo;
import com.ebank.ebanking2.util.RecuPdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@Service
public class VirementService{
    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private RecuPdfGenerator recuPdfGenerator;

    @Autowired
    private VirementRepo virementRepo;

    @Autowired
    private VirementMapper virementMapper;
    @Autowired
    private CompteRepo compteRepo;

    public VirementResDTO effectuerVirement(VirementDTO dto) {

        Compte cEmetteur = compteRepo.findById(dto.getCompteEmetteur())
                .orElseThrow(() -> new RuntimeException("Émetteur non trouvé avec l'ID: " + dto.getCompteEmetteur()));
        Compte cRecepteur = compteRepo.findById(dto.getCompteRecepteur())
                .orElseThrow(() -> new RuntimeException("Récepteur non trouvé avec l'ID: " + dto.getCompteRecepteur()));
        if(cEmetteur instanceof CEpargne){
            throw new RuntimeException("le compte emetteur est epargne, vous ne pouver pas initier virement!");
        }
        if(cRecepteur instanceof CEpargne){
            throw new RuntimeException("le compte recepteur est epargne, vous ne pouver pas initier virement!");
        }
        CCourant ccEmetteur = (CCourant) cEmetteur;
        CCourant ccRecepteur = (CCourant) cRecepteur;
        if (ccEmetteur.getStatus() != StatusCompte.ACTIF || ccRecepteur.getStatus() != StatusCompte.ACTIF) {
            throw new RuntimeException("Le compte émetteur ou récepteur n'est pas actif");
        }
        if (ccEmetteur.getSolde() < dto.getMontant()) {
            throw new RuntimeException("Solde insuffisant sur le compte émetteur");
        }
        Virement virement = virementMapper.toEntity(dto);
        virement.setCompteEmetteur(ccEmetteur);
        virement.setCompteRecepteur(ccRecepteur);
        ccRecepteur.getVirementsRecu().add(virement);
        ccEmetteur.getVirementsEmis().add(virement);
        cEmetteur.setSolde(cEmetteur.getSolde() - dto.getMontant());
        cRecepteur.setSolde(cRecepteur.getSolde() + dto.getMontant());


        virementRepo.save(virement);
        compteRepo.save(cEmetteur);
        compteRepo.save(cRecepteur);
        return virementMapper.toResDTO(virement);
    }
    private void validateAccountStatus(CCourant emetteur, CCourant recepteur) {
        if (emetteur.getStatus() != StatusCompte.ACTIF || recepteur.getStatus() != StatusCompte.ACTIF) {
            throw new IllegalStateException("Les deux comptes doivent être actifs pour effectuer un virement.");
        }
    }

    private void validateSolde(CCourant emetteur, double montant) {
        if (emetteur.getSolde() < montant) {
            throw new IllegalArgumentException("Solde insuffisant sur le compte émetteur.");
        }
    }
    private CCourant getCourantAccount(Long compteId, String role) {
        Compte compte = compteRepo.findById(compteId)
                .orElseThrow(() -> new IllegalArgumentException("Compte " + role + " introuvable avec l'ID : " + compteId));

        if (compte instanceof CEpargne) {
            throw new IllegalArgumentException("Le compte " + role + " est un compte épargne : virement non autorisé.");
        }

        if (!(compte instanceof CCourant)) {
            throw new IllegalStateException("Le compte " + role + " n'est pas de type courant.");
        }

        return (CCourant) compte;
    }
    @Transactional
    public VirementResDTO executeVirement(VirementDTOrib dto) throws IOException {
        if(dto.getCompteEmetteur().equals(dto.getCompteRecepteur())){
            throw new IllegalArgumentException("Le compte émetteur et récepteur ne peuvent pas être identiques");
        }
        CCourant emetteur = getCCourantByRib(dto.getCompteEmetteur());
        CCourant recepteur = getCCourantByRib(dto.getCompteRecepteur());

        validateAccountStatus(emetteur, recepteur);
        validateSolde(emetteur, dto.getMontant());

        Virement virement = virementMapper.toEntity(dto);

        virement.setCompteEmetteur(emetteur);
        virement.setCompteRecepteur(recepteur);

        emetteur.setSolde(emetteur.getSolde() - dto.getMontant());
        recepteur.setSolde(recepteur.getSolde() + dto.getMontant());

        emetteur.getVirementsEmis().add(virement);
        recepteur.getVirementsRecu().add(virement);

        virementRepo.save(virement);
        compteRepo.save(emetteur);
        compteRepo.save(recepteur);
        byte[] recuPdf = recuPdfGenerator.generate(virement);


        try {
            recuPdf = recuPdfGenerator.generate(virement);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erreur lors de la génération du PDF : " + e.getMessage(), e);
        }

        try {
            Files.createDirectories(Paths.get("recus"));
            Path path = Paths.get(System.getProperty("user.dir"), "recus", "recu_virement_" + virement.getId() + ".pdf");
            Files.write(path, recuPdf);
            System.out.println("PDF créé: " + path.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du PDF", e);
        }

        return virementMapper.toResDTO(virement);
    }
    public CCourant getCCourantByRib(String rib){
        return compteRepo.findByRib(rib).orElseThrow();
    }
    public Page<VirementResDTO> getAllVirementByEmetteurCompteIdOrRecepteurCompteId(Long eCompteId, Long rCompteId, int offset, int size) {
        Pageable pageable = PageRequest.of(offset, size);
        Compte eCompte=compteRepo.findById(eCompteId).orElseThrow();
        Compte rCompte=compteRepo.findById(rCompteId).orElseThrow();

        return this.virementRepo.findVirementByCompteEmetteurOrCompteRecepteur((CCourant) eCompte, (CCourant) rCompte,pageable).map(virement -> virementMapper.toResDTO(virement));
    }
    public Page<VirementResDTO> getAllVirements(int offset, int size) {
        Pageable pageable = PageRequest.of(offset, size);
        return this.virementRepo.findAll(pageable).map(virement -> virementMapper.toResDTO(virement));
    }
    public Virement getById(Long virementId) throws AccountNotFoundException {
        return virementRepo.findById(virementId)
                .orElseThrow(() -> new AccountNotFoundException("Virement with Id " + virementId + " not found"));
    }
}