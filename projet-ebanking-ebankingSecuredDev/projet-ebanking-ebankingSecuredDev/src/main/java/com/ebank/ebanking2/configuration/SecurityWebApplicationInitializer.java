package com.ebank.ebanking2.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    // This class is required to register Spring Security's filter chain in plain Spring
    // It's automatically handled by Spring Boot, but must be explicit in plain Spring
}