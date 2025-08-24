package com.travelplanner.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TravelRequest {
    private String startDestination;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfPeople;
    private double minBudget;
    private double maxBudget;
    private String travelType;
    private String transportation;
}