package com.example.cybersport_platform.service;

public interface ThreadSafeCounterService {

    long incrementAndGet();

    long getValue();
}
