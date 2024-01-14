package com.io.github.msj.delivery.config;

import com.io.github.msj.delivery.security.user.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return Optional.of(user.getName());
    }

}