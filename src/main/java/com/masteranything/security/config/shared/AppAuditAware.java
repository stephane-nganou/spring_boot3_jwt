package com.masteranything.security.config.shared;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.masteranything.security.dao.User;

public class AppAuditAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        // from org.springframework.security.core.Authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || 
            !authentication.isAuthenticated() || 
            authentication instanceof AnonymousAuthenticationToken    
        ) return Optional.empty();

        var userPrincipal = (User)authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }

}
