package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.RaceConditionDemoRequest;
import com.example.cybersport_platform.dto.response.RaceConditionDemoResponse;

public interface RaceConditionDemoService {

    RaceConditionDemoResponse runDemo(RaceConditionDemoRequest request);
}
