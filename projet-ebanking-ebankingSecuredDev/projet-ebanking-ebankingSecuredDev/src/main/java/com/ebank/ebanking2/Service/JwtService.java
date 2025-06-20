package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.dto.TokenWrapper;
import com.ebank.ebanking2.model.entity.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Service
@PropertySource("classpath:application.properties")
public class JwtService {
    @Autowired
    TokenService tokenService;
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration}")
    private long jwtAccessExpiration;
    @Value("${jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.audience}")
    private String jwtAudience;
    private final String jti = UUID.randomUUID().toString();

//    private String secretKey;
//
//    public JwtService(){
//        secretKey = generateSecretKey();
//    }
//
//    public String generateSecretKey(){
//        try{
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGen.generateKey();
//            System.out.println("Secret key: " + secretKey.toString());
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error generating secret key ", e);
//        }
//    }

    public TokenWrapper generateToken(HttpServletRequest request, String userEmail, long jwtExpiration) {
        return generateToken(request, new HashMap<>(), userEmail, jwtExpiration);
    }

    public TokenWrapper generateToken(HttpServletRequest request, Map<String, Object> extraClaims, String userEmail, long jwtExpiration) {
        long currentTime = System.currentTimeMillis();
        long expiredTime = currentTime + jwtExpiration;
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        String userAgent = request.getHeader("User-Agent");
        String platform = "WEB";

        if (userAgent != null) {
            String ua = userAgent.toLowerCase();
            if (ua.contains("android")) {
                platform = "ANDROID";
            } else if (ua.contains("iphone") || ua.contains("ios")) {
                platform = "IOS";
            }
        }
        Date lastUsedAt = new Date(currentTime);
        Date createdAt = new Date(currentTime);
        Date expiredAt = new Date(expiredTime);

        extraClaims.put("ipAddress", ipAddress);
        extraClaims.put("userAgent", userAgent);
        extraClaims.put("platform", platform);
        extraClaims.put("lastUsedAt", lastUsedAt);

        return new TokenWrapper(Jwts.builder()
                .setClaims(extraClaims)
                .setId(jti)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setSubject(userEmail)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expiredTime))
                .signWith(getkey(), SignatureAlgorithm.HS256)
                .compact(), createdAt, expiredAt, ipAddress, userAgent, platform, lastUsedAt);
    }

    public TokenWrapper generateAccessToken(HttpServletRequest request, Map<String, Object> extraClaims, String userEmail) {
        return generateToken(request, extraClaims, userEmail, jwtAccessExpiration);
    }

    public TokenWrapper generateAccessToken(HttpServletRequest request, String userEmail) {
        return generateToken(request, userEmail, jwtAccessExpiration);
    }

    public TokenWrapper generateRefreshToken(HttpServletRequest request, Map<String, Object> extraClaims, String userEmail) {
        return generateToken(request, extraClaims, userEmail, jwtRefreshExpiration);
    }

    public TokenWrapper generateRefreshToken(HttpServletRequest request, String userEmail) {
        return generateToken(request, userEmail, jwtRefreshExpiration);
    }

    private Key getkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); //SECRET
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //    public boolean validateToken(String token, UserDetails userDetails) {
//        final String userEmail = extractUserEmail(token);
//        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String userEmail = extractUserEmail(token);
            boolean isEmailValid = userEmail.equals(userDetails.getUsername());
            boolean isNotExpired = !isTokenExpired(token);

            System.out.println("Token validation - Email match: " + isEmailValid +
                    ", Not expired: " + isNotExpired);

            return isEmailValid && isNotExpired;
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserEmail(String token) {
        // extract the userEmail from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //    private Claims extractAllClaims(String token){
//        try {
//            return Jwts.parserBuilder()
//                        .setSigningKey(getkey())
//                        .build().parseClaimsJws(token).getBody();
//        } catch (ExpiredJwtException e){
//            Token tokenObj = tokenService.getTokenObjByToken(token);
//            if(tokenObj != null && !tokenObj.isExpired()){
//                tokenObj.setExpired(true);
//                tokenService.updateToken(tokenObj.getId() ,tokenObj);
//            }
//            return null;
//        }
//        //
//    }
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getkey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired, updating database...");
            Token tokenObj = tokenService.getTokenObjByToken(token);
            if (tokenObj != null && !tokenObj.isExpired()) {
                tokenObj.setExpired(true);
                tokenService.updateToken(tokenObj.getId(), tokenObj);
            }
            // Return the claims from expired token so we can still extract user info
            return e.getClaims();
        } catch (Exception e) {
            System.err.println("JWT parsing error: " + e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
