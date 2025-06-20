package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.Cryptoservice;
import com.ebank.ebanking2.model.dto.AcheteDto;
import com.ebank.ebanking2.model.dto.Vendredto;
import com.ebank.ebanking2.model.entity.Crypto;
import com.ebank.ebanking2.repository.Cryptorepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Cryptocontroller {

    private Cryptoservice cryptoservice;
    private Cryptorepo cryptorepo;

    public Cryptocontroller(Cryptoservice cryptoservice, Cryptorepo cryptorepo) {
        this.cryptoservice = cryptoservice;
        this.cryptorepo = cryptorepo;
    }
    @PreAuthorize("hasRole('CLIENT') and @compteService.getClientByCompteRib(#rib).id == authentication.principal.id")
    @PostMapping("/crypto")
    public boolean Crypto(@RequestBody AcheteDto acheteDto,@RequestParam("rib") @P("rib") String rib) {
        return cryptoservice.acheter(acheteDto.getName(),acheteDto.getMontant(),acheteDto.getActuealprisecurrency(),rib);

    }
    @PreAuthorize("hasRole('CLIENT') and @compteService.getClientByCompteRib(#rib).id == authentication.principal.id")
    @PostMapping("/crypto/vendre")
    public boolean Vendre(@RequestBody Vendredto vendredto,@RequestParam("rib") @P("rib") String rib) {
        return cryptoservice.vendre(vendredto.getName(),vendredto.getMontant(),vendredto.getActuealprisecurrency(),rib);

    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/all")
    public List<Crypto> getAll(){
        return cryptoservice.getall();
    }

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @compteService.getClientByCompteId(@cryptoservice.getByCCourantId(#id).ccourant.id).id == authentication.principal.id)")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
         cryptorepo.deleteById(id);
    }
}
