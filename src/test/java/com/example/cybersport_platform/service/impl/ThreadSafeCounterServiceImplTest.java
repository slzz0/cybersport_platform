package com.example.cybersport_platform.service.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadSafeCounterServiceImplTest {

    private final ThreadSafeCounterServiceImpl service = new ThreadSafeCounterServiceImpl();

    @Test
    void incrementAndGetShouldIncreaseValue() {
        assertThat(service.incrementAndGet()).isEqualTo(1L);
        assertThat(service.incrementAndGet()).isEqualTo(2L);
        assertThat(service.getValue()).isEqualTo(2L);
    }
}
