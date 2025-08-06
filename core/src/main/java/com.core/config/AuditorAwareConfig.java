package com.core.config;

import com.core.constants.FConstants;
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
                : FConstants.BASE_USER; // Đảm bảo không null
        return Optional.ofNullable(currentUser);
    }


}
