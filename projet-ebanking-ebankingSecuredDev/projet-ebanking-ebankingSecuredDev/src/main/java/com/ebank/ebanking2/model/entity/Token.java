package com.ebank.ebanking2.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank(message = "User ID must not be blank")
    private Long userId;

    @NotBlank(message = "Token must not be blank")
    @Size(min = 10, max = 1000, message = "Token length must be between 10 and 500 characters")
    @Column(length = 1000)
    private String token;

    @NotNull(message = "Token type must not be null")
    private TokenType tokenType = TokenType.REFRESH;

    private boolean revoked = false;
    private boolean expired = false;

    @NotNull(message = "Created date must not be null")
    @PastOrPresent(message = "Created date cannot be in the future")
    private Date createdAt = new Date();

    @NotNull(message = "Expiration date must not be null")
//    @Future(message = "Expiration date must be in the future")
    private Date expiredAt = new Date();

    private String ipAddress;
    private String userAgent;
    private String platform;
    private Date lastUsedAt;
}
