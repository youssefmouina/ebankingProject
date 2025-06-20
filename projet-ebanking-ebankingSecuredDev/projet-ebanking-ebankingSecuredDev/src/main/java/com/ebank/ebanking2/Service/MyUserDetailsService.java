package com.ebank.ebanking2.Service;

import com.ebank.ebanking2.model.entity.Token;
import com.ebank.ebanking2.model.entity.User;
import com.ebank.ebanking2.model.entity.UserPrincipal;
import com.ebank.ebanking2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         User user = userRepo.findByEmail(email)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> tokens = tokenService.getValidTokensByUserId(user.getId())
                .stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .map(Token::getToken)
                .toList();
         return new UserPrincipal(user,tokens);
    }
}
