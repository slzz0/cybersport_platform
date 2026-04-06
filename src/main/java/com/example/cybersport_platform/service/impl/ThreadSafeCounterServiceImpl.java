package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.service.ThreadSafeCounterService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class ThreadSafeCounterServiceImpl implements ThreadSafeCounterService {

    private final AtomicLong counter = new AtomicLong();

    @Override
    public long incrementAndGet() {
        return counter.incrementAndGet();
    }

    @Override
    public long getValue() {
        return counter.get();
    }
}
