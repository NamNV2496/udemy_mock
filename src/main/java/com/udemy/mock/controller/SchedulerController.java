package com.udemy.mock.controller;

import com.udemy.mock.scheduler.CalculateTotalNAVScheduler;
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
}
