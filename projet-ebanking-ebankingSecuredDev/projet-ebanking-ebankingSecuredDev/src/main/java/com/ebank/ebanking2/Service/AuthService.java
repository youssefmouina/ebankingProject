package com.ebank.ebanking2.Service;


import com.ebank.ebanking2.model.dto.*;
import com.ebank.ebanking2.model.entity.*;
import com.ebank.ebanking2.model.mapper.ClientMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class AuthService {
    @Autowired
    ClientService userService; ////
    @Autowired
    ApplicationContext context;
    @Autowired
    TokenService tokenService;
    @Autowired
    CompteService compteService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ClientMapper clientMapper;
    @Value("${isDevEnvironment}")
    private boolean isDevEnvironment;

    @Autowired
    private AuthenticationManager authenticationManager;

    private void revokeAllUserTokens(Long userId) {
        List<Token> validUserTokens = tokenService.getValidTokensByUserId(userId);
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenService.createTokens(validUserTokens);
    }
    private void saveUserToken(User user, TokenWrapper token) {
        Token tokenObj = new Token();
        tokenObj.setUserId(user.getId());
        tokenObj.setToken(token.getToken());
        tokenObj.setCreatedAt(token.getCreatedAt());
        tokenObj.setExpiredAt(token.getExpiredAt());
        tokenObj.setIpAddress(token.getIpAddress());
        tokenObj.setUserAgent(token.getUserAgent());
        tokenObj.setPlatform(token.getPlatform());
        tokenObj.setLastUsedAt(token.getLastUsedAt());

        tokenService.createToken(tokenObj);
    }
    private void saveUserToken(ClientResDTO user, TokenWrapper token) {
        Token tokenObj = new Token();
        tokenObj.setUserId(user.getId());
        tokenObj.setToken(token.getToken());
        tokenObj.setCreatedAt(token.getCreatedAt());
        tokenObj.setExpiredAt(token.getExpiredAt());
        tokenObj.setIpAddress(token.getIpAddress());
        tokenObj.setUserAgent(token.getUserAgent());
        tokenObj.setPlatform(token.getPlatform());
        tokenObj.setLastUsedAt(token.getLastUsedAt());

        tokenService.createToken(tokenObj);
    }

    public ResponseEntity<?> register(HttpServletRequest request, HttpServletResponse response, ClientDTO user) {
        try {
            ClientResDTO clientA = userService.addClient(user);
            TokenWrapper accessTokenDto = jwtService.generateAccessToken(request, user.getEmail());
            TokenWrapper refreshTokenDto = jwtService.generateRefreshToken(request, user.getEmail());
            saveUserToken(clientA, refreshTokenDto);
            CCourantDTO cCourantDTO = new CCourantDTO(clientA.getId(), 20, StatusCompte.ACTIF, true );
            compteService.saveCCourant(cCourantDTO);

            // Access token cookie
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessTokenDto.getToken())
                    .httpOnly(true)
                    .secure(!isDevEnvironment)
                    .maxAge(Math.max((refreshTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                    .sameSite(isDevEnvironment ? "Lax" : "Strict")
                    .path("/")  // accessible on all paths
                    .build();

            // Refresh token cookie
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshTokenDto.getToken())
                    .httpOnly(true)
                    .secure(!isDevEnvironment)
                    .maxAge(Math.max((refreshTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                    .sameSite(isDevEnvironment ? "Lax" : "Strict")
                    .path("/")
                    .build();

            // User ID cookie (non-HttpOnly, so frontend can read if needed, else remove .httpOnly())
            ResponseCookie userIdCookie = ResponseCookie.from("userId", String.valueOf(clientA.getId()))
                    .httpOnly(true)  // if you want frontend to read userId, set false here
                    .secure(!isDevEnvironment)
                    .maxAge(Math.max((refreshTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                    .sameSite(isDevEnvironment ? "Lax" : "Strict")
                    .path("/")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, userIdCookie.toString());


            return new ResponseEntity<>(AuthenticationResponse.builder()
                    .accessToken(accessTokenDto.getToken())
                    .refreshToken(refreshTokenDto.getToken())
                    .userId(clientA.getId())
                    .build(), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>("failed", HttpStatus.FORBIDDEN);
        }
    }


    public ResponseEntity<?> authenticate(HttpServletRequest request, HttpServletResponse response, UserLoginDTO user) {
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User authenticatedUser = userPrincipal.getUser();
            TokenWrapper accessTokenDto = jwtService.generateAccessToken(request, authenticatedUser.getEmail());
            TokenWrapper refreshTokenDto = jwtService.generateRefreshToken(request, authenticatedUser.getEmail());
            //revokeAllUserTokens(authenticatedUser.getId());
            saveUserToken(authenticatedUser, refreshTokenDto);

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessTokenDto.getToken())
                    .httpOnly(true)
                    .secure(!isDevEnvironment)
                    .maxAge(Math.max((refreshTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                    .sameSite(isDevEnvironment ? "Lax" : "Strict")
                    .path("/")
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshTokenDto.getToken())
                    .httpOnly(true)
                    .secure(!isDevEnvironment)
                    .maxAge(Math.max((refreshTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                    .sameSite(isDevEnvironment ? "Lax" : "Strict")
                    .path("/")
                    .build();

            ResponseCookie userIdCookie = ResponseCookie.from("userId", String.valueOf(userPrincipal.getId()))
                    .httpOnly(true)  // set false if you want frontend to read
                    .secure(!isDevEnvironment)
                    .maxAge(Math.max((refreshTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                    .sameSite(isDevEnvironment ? "Lax" : "Strict")
                    .path("/")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, userIdCookie.toString());

            return new ResponseEntity<>(AuthenticationResponse.builder()
                    .accessToken(accessTokenDto.getToken())
                    .refreshToken(refreshTokenDto.getToken())
                    .userId(userPrincipal.getId())
                    .build(), HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed");
        }
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) { //throws IOException
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = null;
        String userEmail = null;

//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            refreshToken = authHeader.substring(7);
//            userEmail = jwtService.extractUserEmail(refreshToken);
//        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    userEmail = jwtService.extractUserEmail(refreshToken);
                    break;
                }
            }
        }
        if(userEmail != null){
            User user = this.userService.getUserByEmail1(userEmail);
            boolean isTokenValid = (!tokenService.getTokenObjByToken(refreshToken).isExpired() && !tokenService.getTokenObjByToken(refreshToken).isRevoked());
            if(jwtService.validateToken(refreshToken, new UserPrincipal(user)) && isTokenValid){
                TokenWrapper accessTokenDto = jwtService.generateAccessToken(request, user.getEmail());

                ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessTokenDto.getToken())
                        .httpOnly(true)
                        .secure(!isDevEnvironment)
                        .maxAge(Math.max((accessTokenDto.getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                        .sameSite(isDevEnvironment ? "Lax" : "Strict")
                        .path("/")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

                return new ResponseEntity<>(AuthenticationResponse.builder()
                        .userId(user.getId())
                        .accessToken(accessTokenDto.getToken())
                        .refreshToken(refreshToken)
                        .build(), HttpStatus.OK);
                }
        }
        return new ResponseEntity<>("token not refreshed", HttpStatus.FORBIDDEN);
    }
}
