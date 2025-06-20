package com.ebank.ebanking2.controller;

import com.ebank.ebanking2.Service.AuthService;
import com.ebank.ebanking2.model.dto.ClientDTO;
import com.ebank.ebanking2.model.dto.UserLoginDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> registerClient(HttpServletRequest request, HttpServletResponse response, @RequestBody ClientDTO user) {
        return authService.register(request, response, user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody UserLoginDTO user) {
        return authService.authenticate(request, response, user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Déconnecté avec succès.");
    }
}
