package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.entity.Token;
import com.ebank.ebanking2.repository.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    @Value("${isDevEnvironment}")
    private boolean isDevEnvironment;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        String authHeader = request.getHeader("Authorization");
        String refreshToken;

//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            Token tokenObj = tokenRepository.findByToken(token);
//            if (tokenObj != null){
//                tokenObj.setExpired(true);
//                tokenObj.setRevoked(true);
//                tokenRepository.save(tokenObj);
//            }
//        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    Token tokenObj = tokenRepository.findByToken(refreshToken);
                    if (tokenObj != null){
                        tokenObj.setExpired(true);
                        tokenObj.setRevoked(true);
                        tokenRepository.save(tokenObj);
                    }
                    break;
                }
            }
        }
        ResponseCookie clearRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(!isDevEnvironment)
                .path("/")
                .maxAge(0)
                .sameSite(isDevEnvironment ? "Lax" : "Strict")
                .build();
        ResponseCookie clearAccessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(!isDevEnvironment)
                .path("/")
                .maxAge(0)
                .sameSite(isDevEnvironment ? "Lax" : "Strict")
                .build();
        ResponseCookie clearUserIdCookie = ResponseCookie.from("userId", "")
                .httpOnly(true)
                .secure(!isDevEnvironment)
                .path("/")
                .maxAge(0)
                .sameSite(isDevEnvironment ? "Lax" : "Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearAccessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearUserIdCookie.toString());

    }
}
