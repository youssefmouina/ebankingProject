package com.ebank.ebanking2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenWrapper {
    private String token;
    private Date createdAt = new Date();
    private Date expiredAt = new Date();
    private String ipAddress;
    private String userAgent;
    private String platform;
    private Date lastUsedAt;
}
