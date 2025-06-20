package com.ebank.ebanking2.configuration;

import com.ebank.ebanking2.Service.AuthService;
import com.ebank.ebanking2.Service.JwtService;
import com.ebank.ebanking2.Service.MyUserDetailsService;
import com.ebank.ebanking2.Service.TokenService;
import com.ebank.ebanking2.model.dto.TokenWrapper;
import com.ebank.ebanking2.model.entity.Token;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.aspectj.weaver.bcel.ExceptionRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class JwtFilter extends OncePerRequestFilter {
    @Value("${isDevEnvironment}")
    private boolean isDevEnvironment;
    @Autowired
    JwtService jwtService;
    @Autowired
    ApplicationContext context;
    @Autowired
    TokenService tokenService;
    @Autowired
    AuthService authService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        System.out.println("=== JWT FILTER EXECUTING ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());

        String accessToken = null;
        String refreshToken = null;
        String userEmail = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    accessToken = cookie.getValue();
                    System.out.println("accessToken: " + accessToken);
                }
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    System.out.println("refreshToken: " + refreshToken);
                }
            }
        }
        if (accessToken != null) {
            try {
                System.out.println("okokok");
                userEmail = jwtService.extractUserEmail(accessToken);
            } catch (Exception e) {
                System.err.println("Error extracting email from access token: " + e.getMessage());
                if(refreshToken != null){
                    try{
                        userEmail = jwtService.extractUserEmail(refreshToken);
                    } catch (Exception ex) {
                        System.err.println("Error extracting email from refresh token: " + e.getMessage());
                    }
                }
            }
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Attempting to authenticate user: " + userEmail);

            try {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userEmail);
                System.out.println("User loaded: " + userDetails.getUsername());
                System.out.println("User authorities: " + userDetails.getAuthorities());

                if (accessToken != null && jwtService.validateToken(accessToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Authentication set successfully!");
                    System.out.println("Authenticated user: " + SecurityContextHolder.getContext().getAuthentication().getName());
                    System.out.println("User authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                } else {
                    System.out.println("refreshToken PEOE");
                    try {
                        System.out.println("refreshToken PEOE 1");
                        if (refreshToken != null && jwtService.validateToken(refreshToken, userDetails)) {
                            System.out.println("refreshToken PEOE 2");
                            Token tokenObj = tokenService.getTokenObjByToken(refreshToken);
                            if (!tokenObj.isExpired() && !tokenObj.isRevoked()) {
                                System.out.println("refreshToken PEOE 3");
                                // Generate a new access token
                                TokenWrapper newAccessToken = jwtService.generateAccessToken(request, userEmail);

                                // Set new access token cookie
                                ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken.getToken())
                                        .httpOnly(true)
                                        .secure(!isDevEnvironment)
                                        .maxAge(Math.max((tokenService.getTokenObjByToken(refreshToken).getExpiredAt().getTime() - System.currentTimeMillis()) / 1000, 0))
                                        .sameSite(isDevEnvironment ? "Lax" : "Strict")
                                        .path("/")
                                        .build();
                                response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

                                // Set authentication context
                                UsernamePasswordAuthenticationToken authToken =
                                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authToken);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error during refresh token processing: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Authentication error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        Authentication finalAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Final authentication: " + (finalAuth != null ? finalAuth.getName() : "null"));
        System.out.println("=== END JWT FILTER ===");

        filterChain.doFilter(request, response);
    }
}
