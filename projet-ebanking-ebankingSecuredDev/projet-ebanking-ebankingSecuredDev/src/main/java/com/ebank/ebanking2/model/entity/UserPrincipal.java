package com.ebank.ebanking2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private User user;
    private List<String> tokens;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Method 1: Use class name
        String role = user.getClass().getSimpleName().toUpperCase();

        // Method 2: Alternative - if you want to use discriminator value directly
        // You could add a method to get discriminator value, or check instance type
        if (user instanceof Employee) {
            role = "EMPLOYEE";
        } else if (user instanceof Client) {
            role = "CLIENT";
        } else {
            // Fallback to class name
            role = user.getClass().getSimpleName().toUpperCase();
        }

        System.out.println("User type: " + user.getClass().getName());
        System.out.println("Determined role: " + role);

        if (role.isEmpty()) {
            throw new IllegalStateException("User has no role assigned");
        }

        String authority = "ROLE_" + role;
        System.out.println("Final authority: " + authority);

        return List.of(new SimpleGrantedAuthority(authority));
    }

    // CRITICAL: This method is used by @PreAuthorize expressions like authentication.principal.id
    public Long getId() {
        System.out.println("=== getId() called, returning: " + user.getId() + " ===");
        return user.getId();
    }

    public boolean ownsToken(String token) {
        return tokens != null && tokens
                .stream()
                .anyMatch(t -> t.equals(token));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}