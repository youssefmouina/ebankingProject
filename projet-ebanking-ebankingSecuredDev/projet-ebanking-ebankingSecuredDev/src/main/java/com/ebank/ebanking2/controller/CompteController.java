package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.CompteService;
import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.Compte;
import com.ebank.ebanking2.model.entity.StatusCompte;
import com.ebank.ebanking2.repository.CompteRepo;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.method.P;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comptes")
@CrossOrigin(origins = "http://localhost:4200/")
public class CompteController {
    @Autowired
    private CompteService compteService;
    @Autowired
    private CompteRepo compteRepo;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("{status}/{type}")
    public ResponseEntity<List<?>> getAll(@PathVariable("status") String status, @PathVariable("type") String type) {
        return ResponseEntity.ok(compteService.get(type,status));
    }


    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @compteService.getClientByCompteRib(#rib).id == authentication.principal.id)")
    @GetMapping("solde/{rib}")
    public double getSolde(@PathVariable("rib") @P("rib") String rib){
        return compteService.getSolde(rib);
    }




    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @compteService.getClientByCompteId(#id).id == authentication.principal.id)")
    @GetMapping("/compte/{id}")
    public ResponseEntity<Compte> getById(@PathVariable Long id) {
        return ResponseEntity.ok(compteService.getById(id));
    }
//
//    @GetMapping("/client/{clientId}/{type}/{status}")
//    public ResponseEntity<List<?>> getByClientId(@PathVariable("clientId") Long clientId, @PathVariable("type") String type, @PathVariable("status") String status) {
//        return ResponseEntity.ok(compteService.getByClientId(clientId,type,status));
//    }
//

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #clientId == authentication.principal.id)")
    @GetMapping("/client/{clientId}/{type}/{status}")
    public ResponseEntity<List<?>> getByClientId(@PathVariable("clientId") @P("clientId") Long clientId, @PathVariable("type") String type, @PathVariable("status") String status) {
        return ResponseEntity.ok(compteService.getByClientId(clientId, type, status));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/{clientId}/{type}/{status}")
    public ResponseEntity<List<?>> getComptes(@PathVariable("clientId") Long clientId, @PathVariable("type") String type, @PathVariable("status") String status) {
        return ResponseEntity.ok(compteService.getComptes(clientId,type,status));
    }

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #dto.clientId == authentication.principal.id)")
    @PostMapping("/comptecourant")
    public ResponseEntity<CCourantResDTO> create(@RequestBody @P("dto") CCourantDTO dto) {
        System.out.println("=== CREATE COMPTE COURANT ===");
        System.out.println("DTO clientId: " + dto.getClientId());
        System.out.println("Current user ID: " +
                (org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() != null ?
                        ((com.ebank.ebanking2.model.entity.UserPrincipal)org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId() : "null"));
        return ResponseEntity.ok(compteService.saveCCourant(dto));
    }
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #dto.clientId == authentication.principal.id)")
    @PostMapping("/compteepargne")
    public ResponseEntity<CEpargneResDTO> create(@RequestBody @P("dto") CEpargneDTO dto) {
        return ResponseEntity.ok(compteService.saveCEpargne(dto));
    }

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #id == authentication.principal.id)")
    @GetMapping("/getallbyid/{id}/{type}/{status}")
    public String getallbyid(@PathVariable("id") @P("id") Long id, @PathVariable("type") String type, @PathVariable("status")String status){
        return compteService.getAllComptes(id,type,status);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #clientId == authentication.principal.id)")
    @GetMapping("client/{clientId}/cCourant")
    public ResponseEntity<List<CCourantResDTO>> getCCourantByClientId(@PathVariable("clientId") @P("clientId") Long clientId) {
        return ResponseEntity.ok(new ArrayList<>());
    }
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @compteService.getClientByCompteRib(#rib).id == authentication.principal.id)")
    @GetMapping("soldee/{rib}")
    public double getSoldee( @PathVariable("rib") @P("rib") String rib){
        return compteService.getSolde(rib);
    }


    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @compteService.getClientByCompteId(#accountId).id == authentication.principal.id)")
    @PostMapping("activeDotation/{accountId}/{autorisePaiementEnLigne}")
    public boolean changeDotationStatus(@PathVariable("accountId") @P("accountId") Long accountId,@PathVariable("autorisePaiementEnLigne") boolean autorisePaiementEnLigne){
        return compteService.changeDotationStatus(accountId,autorisePaiementEnLigne);
    }
    @GetMapping("okok")
    public  List<Compte> getComptes(){
        return compteRepo.findByClientIdAndStatus((long) 1, StatusCompte.ACTIF);
    }


}
