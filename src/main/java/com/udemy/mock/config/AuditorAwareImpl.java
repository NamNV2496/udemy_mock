package com.udemy.mock.config;

import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

@Service
public class AuditorAwareImpl implements AuditorAware<String> {

    @SneakyThrows
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("system");
    }
}