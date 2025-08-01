package com.core.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


@EnableJpaAuditing
public class AuditorAwareConfig implements AuditorAware<String> {
//    set data cho BaseEntity
    @Override
    public Optional<String> getCurrentAuditor() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymous"; // Đảm bảo không null
        return Optional.ofNullable(currentUser);
    }


}
