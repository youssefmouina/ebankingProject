package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.*;

import com.ebank.ebanking2.model.entity.*;
import com.ebank.ebanking2.model.mapper.CompteMapper;
import com.ebank.ebanking2.repository.CCourantRepo;
import com.ebank.ebanking2.repository.ClientRepo;
import com.ebank.ebanking2.repository.CompteRepo;
import com.ebank.ebanking2.repository.UserRepo;
import com.ebank.ebanking2.util.RibGenerator;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompteService{
    @Autowired
    private CompteRepo compteRepo;
    @Autowired
    private CompteMapper compteMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private RibGenerator ribGenerator;
    @Autowired
    private CCourantRepo cCourantRepo;
    @Transactional

    public double getSolde( String rib){
        return compteRepo.findByRib(rib).get().getSolde();
    }

    @Transactional
    public CCourantResDTO saveCCourant(CCourantDTO cCourantDTO) {
        Client client= clientRepo.findById(cCourantDTO.getClientId()).orElseThrow(() -> new RuntimeException("Client not found"));
        CCourant ccourant = compteMapper.toEntity(cCourantDTO);
        Optional<Compte> lastCompte = compteRepo.getFirstByOrderByIdDesc();
        String rib;
        if(lastCompte.isPresent()){
            rib= ribGenerator.generateNextId(lastCompte.get().getRib());
        }else{
            rib= ribGenerator.generateNextId(null);
        }
        ccourant.setRib(rib);
        ccourant.setClient(client);
        client.getComptes().add(ccourant);
        compteRepo.save(ccourant);
        return compteMapper.toResDTO(ccourant);
    }
    @Transactional
    public CEpargneResDTO saveCEpargne(CEpargneDTO cEpargneDTO) {
        Client client= clientRepo.findById(cEpargneDTO.getClientId()).orElseThrow(() -> new RuntimeException("Client not found"));
        CEpargne cEpargne = compteMapper.toEntity(cEpargneDTO);
        cEpargne.setClient(client);
        Optional<Compte> lastCompte = compteRepo.getFirstByOrderByIdDesc();
        String rib;
        if(lastCompte.isPresent()){
            rib= ribGenerator.generateNextId(lastCompte.get().getRib());
        }else{
            rib= ribGenerator.generateNextId(null);
        }
        cEpargne.setRib(rib);
        client.getComptes().add(cEpargne);
        compteRepo.save(cEpargne);
        return compteMapper.toResDTO(cEpargne);
    }

    public String getAllComptes(
            Long id,
            String status,
             String type) {

        // Validation
        System.out.println(id.toString()+" "+status+" "+type);

        // Get and format results
        List<?> comptes = getByClientId(id, status, type);
        for (Object compte : comptes) {
            System.out.println(compte.toString());
        }
        if (comptes.isEmpty()) {
            return String.format(
                    "Aucun compte trouvé pour id=%d (type=%s, status=%s)",
                    id, status, type
            );
        }
        String result = formatAccounts(comptes);
        System.out.println(result);
        return  result;
    }
    private String formatAccounts(List<?> comptes) {
        StringBuilder sb = new StringBuilder("Vos comptes:\n\n");
        comptes.forEach(c -> {
            if (c instanceof CompteResDTO dto) {
                sb.append(String.format("""
                • Type: %s
                  RIB: %s
                  Solde: %.2f
                  Statut: %s
                ------------------------
                """,
                        dto.getAccountType(),
                        dto.getRib(),
                        dto.getSolde(),
                        dto.getStatus()
                ));
            }
        });
        return sb.toString();
    }
    public List<?> getByClientId(Long clientId,String type, String status) {
        List<Compte> comptes = compteRepo.findByClientId(clientId);
        return filterListCompte(comptes, type, status);
    }
    public List<?> filterListCompte(List<Compte> comptes, String type, String status){
        List<?> returnedList = new ArrayList<>();
        switch (status.toUpperCase()) {
            case "TOUT":
                returnedList=comptes;
                break;
            case "ACTIF":
                returnedList= comptes.stream()
                        .filter(c->c.getStatus().equals(StatusCompte.valueOf(status)))
                        .toList();
                break;
            case "BLOQUE":
            case "FERME":
                returnedList= comptes.stream()
                        .filter(c->c.getStatus().equals(StatusCompte.valueOf(status)))
                        .toList();
                break;
            default:
                returnedList =comptes;
        }
        switch (type.toLowerCase()) {
            case "compte":
                break;
            case "ccourant":
                returnedList= returnedList.stream()
                        .filter(c -> c instanceof CCourant)
                        .map(c -> (CCourant) c)
                        .map(compteMapper::toResDTO)
                        .toList();
                break;
            case "cepargne":
                returnedList= returnedList.stream()
                        .filter(c -> c instanceof CEpargne)
                        .map(c -> (CEpargne) c)
                        .map(compteMapper::toResDTO)
                        .toList();
                System.out.println("returned: \n"+returnedList);
                break;
            default:
                returnedList= returnedList.stream()
                        .map(c->(Compte) c)
                        .map(compteMapper::toCompteResDTO)
                        .toList();

                break;
        }


        return returnedList;
    }
    public List<?> getComptes(Long clientId,String type, String status) {
        List<Compte> comptes = compteRepo.findByClientId(clientId);
        return filterListCompte(comptes, type,status);
    }
    public List<?> get(String type, String status) {
        List<Compte> comptes = compteRepo.findAll();
        return filterListCompte(comptes, type, status);
    }
    CCourant getCompteByRib(String rib) {
        return compteRepo.findByRib(rib).orElseThrow(() -> new RuntimeException("Compte not found"));
    }
    public Compte getById(Long id) {
        return compteRepo.findById(id).get();
    }
    public CCourantResDTO diminuerSolde(CCourant courant,double montant){
        double newSolde = courant.getSolde()-montant;
        courant.setSolde(newSolde);
        compteRepo.save(courant);
        return compteMapper.toResDTO(courant);
    }


    @Transactional
    public boolean changeDotationStatus(Long accountId,boolean autorisePaiementEnLigne) {
        CCourant compte = cCourantRepo.getById(accountId);
        compte.setAutorisePaiementEnLigne(autorisePaiementEnLigne);
        cCourantRepo.save(compte);
        return true;
    }
    public Client getClientByCompteId(Long compteId) throws AccountNotFoundException {
        return compteRepo.findById(compteId)
                .map(Compte::getClient)
                .orElseThrow(() -> new AccountNotFoundException("Compte with ID " + compteId + " not found"));
    }
    public Client getClientByCompteRib(String rib) throws AccountNotFoundException {
        return compteRepo.findByRib(rib)
                .map(Compte::getClient)
                .orElseThrow(() -> new AccountNotFoundException("Compte with Rib " + rib + " not found"));
    }
}