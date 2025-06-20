package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.TokenService;
import com.ebank.ebanking2.model.entity.Token;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final TokenService tokenService;
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<Token>> getTokens(){
        List<Token> tokens = tokenService.getTokens();
        return ResponseEntity.ok(tokens);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<Token> getTokenById(@PathVariable Long id){
        Token token = tokenService.getTokenById(id);
        return ResponseEntity.ok(token);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Token> createToken(@Validated @RequestBody Token tokenInfo){
        Token token = tokenService.createToken(tokenInfo);
        URI location = URI.create("/api/token/" + token.getId());
        return ResponseEntity.created(location).body(token);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Token> updateToken(@PathVariable Long id, @Validated @RequestBody Token tokenInfo){
        Token token = tokenService.updateToken(id, tokenInfo);
        return ResponseEntity.ok(token);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable Long id){
        tokenService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('EMPLOYEE') or (hasRole('CLIENT') and #userId == authentication.principal.id)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Token>> getTokensByUserId(@PathVariable @P("userId") Long userId){
        List<Token> tokens = tokenService.getTokensByUserId(userId);
        return ResponseEntity.ok(tokens);
    }
    @PreAuthorize("hasAnyRole('EMPLOYEE') or (hasRole('CLIENT') and #userId == authentication.principal.id)")
    @GetMapping("/validUser/{userId}")
    public ResponseEntity<List<Token>> getValidatedTokensByUserId(@PathVariable @P("userId") Long userId){
        List<Token> tokens = tokenService.getValidTokensByUserId(userId);
        return ResponseEntity.ok(tokens);
    }
    @PreAuthorize("hasAnyRole('EMPLOYEE') or (hasRole('CLIENT') and authentication.principal.ownsToken(#token))")
    @GetMapping("/tk/{token}")
    public ResponseEntity<Token> getTokenObjByToken(@PathVariable @P("token") String token){
        Token tokenObj = tokenService.getTokenObjByToken(token);
        return ResponseEntity.ok(tokenObj);
    }
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    @PostMapping("/tokens")
    public ResponseEntity<List<Token>> createTokens(@Valid @RequestBody List<@Valid Token> tokens){
        List<Token> tokensObj = tokenService.createTokens(tokens);
//        URI location = URI.create("/api/token/" + IntStream.range(0, tokensObj.size())
//                .mapToObj(i -> (i + 1) + "token=" + tokensObj.get(i).getId())
//                .collect(Collectors.joining("&")));
        return ResponseEntity.status(HttpStatus.CREATED).body(tokensObj); //ResponseEntity.created(location).body(tokensObj);
    }
}
