package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.MaybeClientService;
import com.ebank.ebanking2.model.dto.MaybeClientDTO;
import com.ebank.ebanking2.model.dto.MaybeClientWithEmailTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/maybeClient")
public class MaybeClientController {
    @Autowired
    private MaybeClientService maybeClientService;

    @PostMapping("emailSend/token")
    public ResponseEntity<Boolean> generateTokenByEmail(@RequestBody MaybeClientDTO maybeClientDTO){
        return new ResponseEntity<>(maybeClientService.generateTokenByEmail(maybeClientDTO), HttpStatus.OK);
    }
    @PostMapping("checkToken")
    public ResponseEntity<Boolean> checkEmailToken(@RequestBody MaybeClientWithEmailTokenDTO maybeClientWithEmailTokenDTO){
        return new ResponseEntity<>(maybeClientService.checkEmailToken(maybeClientWithEmailTokenDTO), HttpStatus.OK);
    }
}
