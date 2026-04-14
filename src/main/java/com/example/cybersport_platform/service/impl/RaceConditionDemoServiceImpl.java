package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.RaceConditionDemoRequest;
import com.example.cybersport_platform.dto.response.RaceConditionDemoResponse;
import com.example.cybersport_platform.service.RaceConditionDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class RaceConditionDemoServiceImpl implements RaceConditionDemoService {

    @Override
    public RaceConditionDemoResponse runDemo(RaceConditionDemoRequest request) {
        int threadCount = request.getThreadCount();
        int incrementsPerThread = request.getIncrementsPerThread();
        int expectedValue = threadCount * incrementsPerThread;

        int unsafeValue = runUnsafeScenario(threadCount, incrementsPerThread);
        int synchronizedValue = runScenario(threadCount, incrementsPerThread, new SynchronizedCounter());
        int atomicValue = runScenario(threadCount, incrementsPerThread, new AtomicCounter());

        RaceConditionDemoResponse response = new RaceConditionDemoResponse(
                threadCount,
                incrementsPerThread,
                expectedValue,
                unsafeValue,
                synchronizedValue,
                atomicValue
        );
        log.info(
                "Race condition demo result: threads={}, incrementsPerThread={}, expectedValue={}, unsafeValue={}, "
                        + "synchronizedValue={}, atomicValue={}",
                response.getThreadCount(),
                response.getIncrementsPerThread(),
                response.getExpectedValue(),
                response.getUnsafeValue(),
                response.getSynchronizedValue(),
                response.getAtomicValue()
        );
        return response;
    }

    private int runUnsafeScenario(int threadCount, int incrementsPerThread) {
        int lowestObservedValue = Integer.MAX_VALUE;
        for (int attempt = 0; attempt < 3; attempt++) {
            int attemptValue = runScenario(threadCount, incrementsPerThread, new UnsafeCounter());
            lowestObservedValue = Math.min(lowestObservedValue, attemptValue);
        }
        return lowestObservedValue;
    }

    private int runScenario(int threadCount, int incrementsPerThread, IncrementCounter counter) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < threadCount; i++) {
                futures.add(executorService.submit(() -> incrementCounter(counter, incrementsPerThread)));
            }

            waitForCompletion(futures);
            return counter.get();
        } finally {
            executorService.shutdown();
        }
    }

    private void incrementCounter(IncrementCounter counter, int incrementsPerThread) {
        for (int i = 0; i < incrementsPerThread; i++) {
            counter.increment();
        }
    }

    private void waitForCompletion(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Race condition demo was interrupted", exception);
            } catch (ExecutionException exception) {
                throw new IllegalStateException("Race condition demo failed", exception);
            }
        }
    }

    private interface IncrementCounter {
        void increment();

        int get();
    }

    private static class UnsafeCounter implements IncrementCounter {

        private int value;

        @Override
        public void increment() {
            int currentValue = value;
            Thread.yield();
            value = currentValue + 1;
        }

        @Override
        public int get() {
            return value;
        }
    }

    private static class SynchronizedCounter implements IncrementCounter {

        private int value;

        @Override
        public synchronized void increment() {
            value++;
        }

        @Override
        public synchronized int get() {
            return value;
        }
    }

    private static class AtomicCounter implements IncrementCounter {

        private final java.util.concurrent.atomic.AtomicInteger value =
                new java.util.concurrent.atomic.AtomicInteger();

        @Override
        public void increment() {
            value.incrementAndGet();
        }

        @Override
        public int get() {
            return value.get();
        }
    }
}
