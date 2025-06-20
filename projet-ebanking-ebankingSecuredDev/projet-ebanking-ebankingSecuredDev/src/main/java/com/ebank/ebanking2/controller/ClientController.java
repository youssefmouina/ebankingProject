package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.ClientService;
import com.ebank.ebanking2.Service.Tokenmailservice;
import com.ebank.ebanking2.mail.Mail;
import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.User;
import com.ebank.ebanking2.model.entity.tokenmail;
import com.ebank.ebanking2.repository.ClientRepo;
import com.ebank.ebanking2.repository.Tokenmailrepo;
import com.ebank.ebanking2.util.EcodeValidator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.method.P;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/clients")
public class ClientController {

    @Autowired
    ClientService clientService;
    @Autowired
    Tokenmailservice tokenmailservice;
    @Autowired
    Tokenmailrepo tokenrepo;
    @Autowired
    Mail mailservice;
    @Autowired
    private ClientRepo clientRepo;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/client")
    public ResponseEntity<ClientResDTO> addClient(@RequestBody ClientDTO clientdto) {
        ClientResDTO cdto= clientService.addClient(clientdto);
        return new ResponseEntity<>(cdto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #email == authentication.principal.username)")
    @GetMapping("/client/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") @P("email") String email) {
        User client = clientService.getUserByEmail(email);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<ClientResDTO>> allClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResDTO> getClient(@PathVariable("id") @P("id") Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and #id == authentication.principal.id)")
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
    }

    @PreAuthorize("hasRole('EMPLOYEE')") // or (hasRole('CLIENT') and #id == authentication.principal.id)
    @PostMapping("/post/token/{id}")
    public boolean posttoken(@PathVariable("id") long id) throws MessagingException {
        String token = tokenmailservice.generateSixDigitToken();
        Client client = clientService.getClientnodtoById(id);
        String tokenma = tokenmailservice.savetoken(token,client).getToken();
        mailservice.javasend(client.getEmail(),tokenma,"validation token","token");
        return true;
    }

    @PreAuthorize("hasRole('CLIENT') and #clientId == authentication.principal.id")
    @PostMapping("/emailSend/eCode/token/{clientId}")
    public boolean sendTokenForEcode(@PathVariable("clientId") @P("clientId") Long clientId) throws MessagingException {
        String token = tokenmailservice.generateSixDigitToken();
        Client client = clientService.getClientnodtoById(clientId);
        String savedToken = tokenmailservice.savetoken(token, client).getToken();
        mailservice.sendTokenEmail(client.getEmail(), savedToken, "validation token pour E-Code","E-Code");
        return true;
    }

    @PreAuthorize("hasRole('EMPLOYEE')") // or (hasRole('CLIENT') and #id == authentication.principal.id)
    @PutMapping("/update/{id}")
    public boolean update(@RequestBody Clientchangedto clientchangedto, @RequestParam("token") String token, @PathVariable("id") @P("id") Long id) {
        boolean validation=tokenmailservice.validateToken(token,id);
        if (validation){
            clientService.updateclient(id, clientchangedto);
            return true;
        }
        else{
            return false;
        }
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/alltoken")
    public List<tokenmail>tokenmail(){
        return tokenrepo.findAll();
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/delete/token/{id}")
    public void delete(@PathVariable("id") long token) {
        tokenrepo.deleteById(token);
    }
    @PreAuthorize("hasRole('EMPLOYEE')") // or (hasRole('CLIENT') and #id == authentication.principal.id)
    @PutMapping("/update/client/{id}")
    public boolean updateclient(@RequestBody Clientchangedto clientchangedto, @PathVariable("id") long id) {
        clientService.updateclient(id, clientchangedto);
        return true;
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/validate/{id}")
    public void validate(@PathVariable("id")long id){
        Client client = clientService.getClientnodtoById(id);
        client.setValid(true);
        clientRepo.save(client);

    }

    @PostMapping("registry/emailSend/recoveryToken")
    public ResponseEntity<Boolean> generateRecoveryPasswordToken(@RequestBody EmailDTO emailDTO){
        return new ResponseEntity<>(clientService.generateRecoveryPasswordToken(emailDTO.getEmail()), HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CLIENT') and @clientService.getUserByEmail(#changePasswordDTO.email).id = authentication.principal.id)")
    @PutMapping("registry/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestBody @P("changePasswordDTO") ChangePasswordDTO changePasswordDTO){
        return new ResponseEntity<>(clientService.changePassword(changePasswordDTO), HttpStatus.OK);
    }

    @PostMapping("registry/checkRecoveryToken")
    public ResponseEntity<Boolean> checkRecoveryToken(@RequestBody CheckRecoveryTokenDTO checkRecoveryTokenDTO){
        return new ResponseEntity<>(clientService.checkRecoveryToken(checkRecoveryTokenDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CLIENT') and #clientId == authentication.principal.id")
    @PostMapping("verifiytoken/ecode/{clientId}")
    public boolean validateTokenForEcode(@RequestParam("ecodeToken") String ecodeToken,
                                         @PathVariable("clientId") @P("clientId") long clientId) throws MessagingException {
        return tokenmailservice.validateToken(ecodeToken, clientId);
    }
    // Vérifier si le token (Ecode) saisi est sécurisé
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("isValid")
    public ResponseEntity<Boolean> validateSecureEcode(@RequestParam("ecode") String ecode) {
        boolean valid = EcodeValidator.isSecureCode(ecode);
        return new ResponseEntity<>(valid, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('CLIENT') and #ecodeDTO.clientId == authentication.principal.id")
    @PostMapping("saveEcode")
    public boolean saveEcode(@RequestBody @P("ecodeDTO") EcodeDTO ecodeDTO) throws MessagingException {
        return clientService.saveEcode(ecodeDTO);
    }

    @PreAuthorize("hasRole('CLIENT') and #ecodeDTO.clientId == authentication.principal.id")
    @PostMapping("verifyEcode")
    public boolean verifyEcode(@RequestBody @P("ecodeDTO") EcodeDTO ecodeDTO) throws MessagingException {
        return clientService.verifyEcode(ecodeDTO);
    }
}
