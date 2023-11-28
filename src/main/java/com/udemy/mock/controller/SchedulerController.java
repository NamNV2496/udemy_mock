package com.udemy.mock.controller;

import com.udemy.mock.scheduler.CalculateTotalNAVScheduler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job")
@RequiredArgsConstructor
public class SchedulerController {

    private final CalculateTotalNAVScheduler job;


    // curl --location 'http://localhost:8080/mock/api/v1/job'
    @GetMapping
    public void triggerJob() {

        job.jobNav();
    }

    // curl --location 'http://localhost:8080/mock/api/v1/job/way2'
    @GetMapping("/way2")
    public void triggerJobWay2() {

        job.jobNavWay2();
    }

    // curl --location 'http://localhost:8080/mock/api/v1/job/calculate-per-day'
    @GetMapping("/calculate-per-day")
    public Map<Long, Integer> calculateEachDay() {

        return job.calculateTotalAmountAccountEachDayQueryNative();
    }
}
