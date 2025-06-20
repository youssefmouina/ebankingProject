package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.Cryptodtoreqacheter;
import com.ebank.ebanking2.model.entity.CCourant;
import com.ebank.ebanking2.model.entity.Crypto;
import com.ebank.ebanking2.model.mapper.CompteMapper;
import com.ebank.ebanking2.repository.CompteRepo;
import com.ebank.ebanking2.repository.Cryptorepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class Cryptoservice {
    @Autowired
    private Cryptorepo cryptorepo;
    @Autowired
    private CompteMapper compteMapper;
    private CompteService compteService;
    private CompteRepo compteRepo;
    public Cryptoservice(Cryptorepo cryptorepo, CompteService compteRepo, CompteRepo compteRepo1) {
        this.cryptorepo = cryptorepo;
        this.compteService = compteRepo;
        this.compteRepo = compteRepo1;
    }
    public boolean acheter(String name,double montant,double actuealprisecurrency,String rib){

         double prix_acheter=montant*actuealprisecurrency;

        double compte=compteService.getSolde(rib);
        if(compte<prix_acheter){
            return false;
        }
        double prix_ajouter=compte-prix_acheter;

        System.out.println("he");
        CCourant compte1=compteRepo.findByRib(rib).get();
        System.out.println(compte1.getId());
        compte1.setSolde(prix_ajouter);
        compteRepo.save(compte1);
        Crypto crypto = new Crypto();
        crypto.setNamecrypto(name);
        crypto.setValueacheter(prix_acheter);
        crypto.setCcourant(compte1);
        cryptorepo.save(crypto);
        return true;
    }
    public boolean vendre(String name,double montant,double actuealprisecurrency,String rib){

        double prix_vendre=montant*actuealprisecurrency;


        double compte=compteService.getSolde(rib);
        double prix_ajouter=compte+prix_vendre;
        System.out.println("he");
        CCourant compte1=compteRepo.findByRib(rib).get();
        System.out.println(compte1.getId());
        compte1.setSolde(prix_ajouter);
        compteRepo.save(compte1);
        Crypto crypto = new Crypto();
        crypto.setNamecrypto(name);
        crypto.setValuevendre(prix_vendre);
        crypto.setCcourant(compte1);
        cryptorepo.save(crypto);
        return true;
    }
    public List<Crypto> getall(){
        return cryptorepo.findAll();
    }
    public Crypto getByCCourantId(Long id){
        return cryptorepo.findByCcourantId(id);
    }
}
