package com.travelplanner.controller;

import com.travelplanner.model.TravelPlan;
import com.travelplanner.model.TravelRequest;
import com.travelplanner.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TravelController {

    @Autowired
    private AIService aiService;

    @PostMapping("/api/generate-plan")
    public ResponseEntity<TravelPlan> generateTravelPlan(@RequestBody TravelRequest request) {
        try {
            TravelPlan plan = aiService.generatePlan(request);
            return new ResponseEntity<>(plan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}