package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.tokenmail;
import com.ebank.ebanking2.repository.Tokenmailrepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class Tokenmailservice {
    private Tokenmailrepo tokenmailrepo;
    private ClientService clientService;

    public Tokenmailservice(Tokenmailrepo tokenmailrepo, ClientService clientService) {
        this.tokenmailrepo = tokenmailrepo;
        this.clientService = clientService;
    }

    public String generateSixDigitToken() {
         return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public tokenmail savetoken(String token, Client client) {
        tokenmail tokens = new tokenmail();
        tokens.setToken(token);
        tokens.setClient(client);
        tokens.setExpriryDate(LocalDateTime.now().plusMinutes(3));
        tokens.setUsed(false);
        tokenmail tokenmail = tokenmailrepo.save(tokens);
        return tokenmail;
    }

    public boolean validateToken(String token, long clientId) {
        Client client = clientService.getClientnodtoById(clientId);
        Optional<tokenmail> tokens = tokenmailrepo.findByTokenAndClient(token, client);
        if (tokens.isEmpty()) {
            return false;
        }
        tokenmail tokenn = tokens.get();
        if (tokenn.isUsed()) {
            return false;
        }
        if (tokenn.getExpriryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        tokenn.setUsed(true);
        tokenmail tokenmail = tokenmailrepo.save(tokenn);
        return true;
    }
}
