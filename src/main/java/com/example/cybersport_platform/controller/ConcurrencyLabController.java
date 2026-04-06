package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.AsyncBusinessTaskRequest;
import com.example.cybersport_platform.dto.request.RaceConditionDemoRequest;
import com.example.cybersport_platform.dto.response.AsyncTaskStartResponse;
import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.dto.response.CounterResponse;
import com.example.cybersport_platform.dto.response.RaceConditionDemoResponse;
import com.example.cybersport_platform.service.AsyncBusinessTaskService;
import com.example.cybersport_platform.service.RaceConditionDemoService;
import com.example.cybersport_platform.service.ThreadSafeCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concurrency")
@Tag(name = "Concurrency Lab", description = "Async tasks, thread-safe counter and race condition demo")
public class ConcurrencyLabController {

    private final AsyncBusinessTaskService asyncBusinessTaskService;
    private final ThreadSafeCounterService threadSafeCounterService;
    private final RaceConditionDemoService raceConditionDemoService;

    @PostMapping("/tasks")
    @Operation(summary = "Start asynchronous business operation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Task accepted"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<AsyncTaskStartResponse> startTask(
            @Valid @RequestBody AsyncBusinessTaskRequest request
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(asyncBusinessTaskService.startTask(request));
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Get asynchronous task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status received"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<AsyncTaskStatusResponse> getTaskStatus(@PathVariable Long taskId) {
        return ResponseEntity.ok(asyncBusinessTaskService.getTaskStatus(taskId));
    }

    @PostMapping("/counter/increment")
    @Operation(summary = "Increment thread-safe counter")
    @ApiResponse(responseCode = "200", description = "Counter incremented")
    public ResponseEntity<CounterResponse> incrementCounter() {
        return ResponseEntity.ok(new CounterResponse(threadSafeCounterService.incrementAndGet()));
    }

    @GetMapping("/counter")
    @Operation(summary = "Get thread-safe counter value")
    @ApiResponse(responseCode = "200", description = "Counter value received")
    public ResponseEntity<CounterResponse> getCounterValue() {
        return ResponseEntity.ok(new CounterResponse(threadSafeCounterService.getValue()));
    }

    @PostMapping("/race-condition/demo")
    @Operation(summary = "Demonstrate race condition and thread-safe solutions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Demo completed"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<RaceConditionDemoResponse> runRaceConditionDemo(
            @Valid @RequestBody RaceConditionDemoRequest request
    ) {
        return ResponseEntity.ok(raceConditionDemoService.runDemo(request));
    }
}
