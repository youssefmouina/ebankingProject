package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.ClientDTO;
import com.ebank.ebanking2.model.dto.ClientResDTO;
import com.ebank.ebanking2.model.dto.Clientchangedto;
import com.ebank.ebanking2.model.dto.EcodeDTO;
import com.ebank.ebanking2.exception.user.UserUpdateException;
import com.ebank.ebanking2.mail.Mail;
import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.Client;
import com.ebank.ebanking2.model.entity.MaybeClient;
import com.ebank.ebanking2.model.entity.User;
import com.ebank.ebanking2.model.mapper.ClientMapper;
import com.ebank.ebanking2.model.mapper.MaybeClientMapper;
import com.ebank.ebanking2.repository.ClientRepo;
import jakarta.mail.MessagingException;
import com.ebank.ebanking2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private MaybeClientService maybeClientService;
    @Autowired
    private MaybeClientMapper maybeClientMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Tokenmailservice tokenmailservice;
    @Autowired
    private Mail mail;
    @Autowired
    private UserRepo userRepo;


    public ClientResDTO addClient(ClientDTO clientDTO) {
        Optional<Client> verifyClient = clientRepo.findByEmail(clientDTO.getEmail());
        if (verifyClient.isPresent()){
            throw new IllegalStateException("this email is already token");
        }
        if (clientDTO.getPassword() == null || clientDTO.getPassword().length() < 8) {
            throw new IllegalStateException("Password must contain at least 8 characters");
        }
        SubMaybeClientDTO subMaybeClientDTO = maybeClientMapper.toDto(clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        System.out.println("okok25541o");

        if (!maybeClientService.getMaybeClientBySubMaybeClient(subMaybeClientDTO).isReadyTobeClient()) {
            System.out.println("okoko1");
            throw new IllegalStateException("The associated MaybeClient is not ready to become a Client.");
        }

        System.out.println(client.getPassword() != null && !client.getPassword().isEmpty());
        if (client.getPassword() != null && !client.getPassword().isEmpty()) {
            System.out.println("okoko2");
            String encryptedPassword = passwordEncoder.encode(client.getPassword());
            client.setPassword(encryptedPassword);
        } else {
            System.out.println("okoko3");
            // Handle the case where the password is null or empty, maybe log a warning
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        System.out.println("okoko4");
        // Save the client to the repository
        clientRepo.save(client);

        System.out.println("okoko5");
        // Return the response DTO
        return clientMapper.toResDTO(client);
    }

    public List<ClientResDTO> getAllClients() {
        return clientRepo.findAll().stream()
                .map(clientMapper::toResDTO)
                .collect(Collectors.toList());
    }
    public ClientResDTO getClientById(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        return clientMapper.toResDTO(client);
    }
    public Client getClientnodtoById(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        return client;
    }

    //ajoutee pour  deleter un tel client
    public void deleteClient(Long id) {
        clientRepo.deleteById(id);
    }
    public void updateclient(Long id,Clientchangedto clientchangedto) {

        Client client=getClientnodtoById(id);
        client.setEmail(clientchangedto.getEmail());
        client.setPhone(clientchangedto.getPhone());
        client.setUsername(clientchangedto.getUsername());
        client.setJob(clientchangedto.getJob());
        clientRepo.save(client);


    }
//    public Client getclientbyEmail(String email) {
//            return clientRepo.findByEmail(email);
//    }
    public Client getclientbyemail(String email) {
        return clientRepo.findByEmail(email).get();
    }
    public Client getUserByEmail(String email) {
        return clientRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User non trouvé avec l'Email: " + email));
    }
    public User getUserByEmail1(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User non trouvé avec l'Email: " + email));
        return user;
    }
//    public boolean changePassword(ChangePasswordDTO changePasswordDTO){
//        Optional<Client> user = null;
//
//        user = clientRepo.findByEmailAndIsRecoveryPasswordTokenVerifiedAndRecoveryPasswordTokenAndRecoveryPasswordTokenExpirationTimeAfter(changePasswordDTO.getEmail(), true, passwordEncoder.encode(changePasswordDTO.getCurrentToken()), LocalDateTime.now());
//        System.out.println("ok752");
//        System.out.println(user);
//        if(user.isPresent()){
//            System.out.println("ok752556");
//            user.get().setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
//            user.get().setRecoveryPasswordTokenVerified(false);
//            user.get().setRecoveryPasswordToken("");
//            try{
//                System.out.println("ok712");
//                Client updatedUser = clientRepo.save(user.get());
//                return true;
//            } catch (Exception e){
//                System.out.println("ok7120000");
//                return false;
////                throw new UserUpdateException("error while updating the client");
//            }
//        } else{
//            System.out.println("524");
//            return false;
//        }
//    }
public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
    if (changePasswordDTO.getNewPassword() == null || changePasswordDTO.getNewPassword().length() < 8) {
        System.out.println("Password must contain at least 8 characters");
        return false;
    }
    Optional<Client> userOpt = clientRepo.findByEmailAndIsRecoveryPasswordTokenVerified(changePasswordDTO.getEmail(), true);

    if (userOpt.isEmpty()) {
        System.out.println("User not found or token not verified");
        return false;
    }

    Client user = userOpt.get();

    boolean tokenMatches = passwordEncoder.matches(
            changePasswordDTO.getCurrentToken(),
            user.getRecoveryPasswordToken()
    );

    boolean tokenNotExpired = user.getRecoveryPasswordTokenExpirationTime().isAfter(LocalDateTime.now());

    if (!tokenMatches || !tokenNotExpired) {
        System.out.println("Token mismatch or expired");
        return false;
    }

    user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
    user.setRecoveryPasswordTokenVerified(false);
    user.setRecoveryPasswordToken("");

    try {
        clientRepo.save(user);
        return true;
    } catch (Exception e) {
        System.out.println("Error saving user");
        return false;
    }
}


//    public Boolean checkRecoveryToken(CheckRecoveryTokenDTO checkRecoveryTokenDTO) {
//        Optional<Client> client = null;
//        client = clientRepo.findByEmailAndIsRecoveryPasswordTokenVerified(checkRecoveryTokenDTO.getEmail(), false);
//        if(client.isPresent()){
//            client.get().setRecoveryPasswordToken(passwordEncoder.encode(client.get().getRecoveryPasswordToken()));
//            try{
//                Client clientFound = clientRepo.findByEmailAndIsRecoveryPasswordTokenVerifiedAndRecoveryPasswordTokenAndRecoveryPasswordTokenExpirationTimeAfter(
//                        client.get().getEmail(), true, client.get().getRecoveryPasswordToken(), LocalDateTime.now());
//                clientFound.setRecoveryPasswordTokenVerified(true);
//                return true;
//            } catch (Exception e){
//                return false;
//            }
//        } else{
//            return false;
//        }
//    }
    public Boolean checkRecoveryToken(CheckRecoveryTokenDTO checkRecoveryTokenDTO) {
        Optional<Client> clientOpt = clientRepo.findByEmail(checkRecoveryTokenDTO.getEmail());

        if (clientOpt.isEmpty()) {
            return (Boolean) false;
        }

        Client client = clientOpt.get();

        // Compare the raw token with the hashed one stored in DB
        boolean matches = passwordEncoder.matches(
                checkRecoveryTokenDTO.getRecoveryPasswordToken(),
                client.getRecoveryPasswordToken()
        );

        boolean notExpired = client.getRecoveryPasswordTokenExpirationTime().isAfter(LocalDateTime.now());

        if (matches && notExpired) {
            client.setRecoveryPasswordTokenVerified(true);
            clientRepo.save(client);
            return (Boolean) true;
        }

        return (Boolean) false;
    }


    public Boolean generateRecoveryPasswordToken(String email) {
        String recoveryPasswordToken = tokenmailservice.generateSixDigitToken();
        Client client = getUserByEmail(email);
        client.setRecoveryPasswordToken(passwordEncoder.encode(recoveryPasswordToken));
        client.setRecoveryPasswordTokenExpirationTime(LocalDateTime.now().plusMinutes(10));
        Client clientSaved = clientRepo.save(client);
        try {
            mail.sendTokenEmail(clientSaved.getEmail(), recoveryPasswordToken,"validation token pour Recovery Password via email", "Email");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return (Boolean) true;
    }
    public boolean verifyEcode(EcodeDTO ecodeDTO) {
        Optional<Client> client=clientRepo.findById(ecodeDTO.getClientId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(ecodeDTO.getCode(), client.get().getCode());
    }
    public boolean saveEcode(EcodeDTO ecodeDTO) {
        Optional<Client> client=clientRepo.findById(ecodeDTO.getClientId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedEcode = encoder.encode(ecodeDTO.getCode());
        client.get().setCode(hashedEcode);
        clientRepo.save(client.get());
        return true;
    }
}