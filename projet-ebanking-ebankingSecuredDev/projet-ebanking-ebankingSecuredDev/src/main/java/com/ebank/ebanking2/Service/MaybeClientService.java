package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.mail.Mail;
import com.ebank.ebanking2.model.dto.MaybeClientDTO;
import com.ebank.ebanking2.model.dto.MaybeClientWithEmailTokenDTO;
import com.ebank.ebanking2.model.dto.SubMaybeClientDTO;
import com.ebank.ebanking2.model.entity.MaybeClient;
import com.ebank.ebanking2.model.mapper.MaybeClientMapper;
import com.ebank.ebanking2.repository.MaybeClientRepo;
import jakarta.mail.MessagingException;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaybeClientService {
    @Autowired
    private Tokenmailservice tokenmailservice;
    @Autowired
    private MaybeClientMapper maybeClientMapper;
    @Autowired
    private MaybeClientRepo maybeClientRepo;
    @Autowired
    private Mail mail;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean generateTokenByEmail(MaybeClientDTO maybeClientDTO){
        MaybeClient maybeClient = maybeClientMapper.toEntity(maybeClientDTO);
        String emailToken = tokenmailservice.generateSixDigitToken();
        maybeClient.setEmailToken(passwordEncoder.encode(emailToken));
        maybeClient.setEmailTokenExpirationTime(LocalDateTime.now().plusMinutes(10));
        MaybeClient maybeClientSaved = maybeClientRepo.save(maybeClient);
        try {
            mail.sendTokenEmail(maybeClient.getEmail(), emailToken,"validation token pour email", "Email");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public Boolean checkEmailToken(MaybeClientWithEmailTokenDTO dto) {
        String rawToken = dto.getEmailToken(); // user input
        String email = dto.getEmail();

        List<MaybeClient> candidates = maybeClientRepo
                .findByEmailAndEmailTokenExpirationTimeAfterOrderByCreatedAtAsc(email, LocalDateTime.now());

        if (candidates == null || candidates.isEmpty()) {
            return false;
        }

        MaybeClient mc = candidates.getLast();
        if (passwordEncoder.matches(rawToken, mc.getEmailToken())) {
            mc.setEmailTokenVerified(true);
            mc.setReadyTobeClient(true);
            maybeClientRepo.save(mc);
            return true;
        }

        return false;
    }
    public MaybeClient getMaybeClientBySubMaybeClient(SubMaybeClientDTO subMaybeClientDTO){
        System.out.println("in");
        List<MaybeClient> maybeClients = maybeClientRepo.findByEmailOrderByCreatedAtAsc(subMaybeClientDTO.getEmail());
        System.out.println("executed");
        System.out.println(maybeClients.getLast());
        return maybeClients.getLast();
    }
}
