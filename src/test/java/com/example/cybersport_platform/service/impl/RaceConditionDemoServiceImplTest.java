package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.RaceConditionDemoRequest;
import com.example.cybersport_platform.dto.response.RaceConditionDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RaceConditionDemoServiceImplTest {

    private final RaceConditionDemoServiceImpl service = new RaceConditionDemoServiceImpl();

    @Test
    void runDemoShouldShowSafeCountersReachExpectedValue() {
        RaceConditionDemoRequest request = new RaceConditionDemoRequest(8, 2_000);

        RaceConditionDemoResponse response = service.runDemo(request);

        assertThat(response.getExpectedValue()).isEqualTo(16_000);
        assertThat(response.getSynchronizedValue()).isEqualTo(response.getExpectedValue());
        assertThat(response.getAtomicValue()).isEqualTo(response.getExpectedValue());
        assertThat(response.getUnsafeValue()).isLessThanOrEqualTo(response.getExpectedValue());
    }
}
