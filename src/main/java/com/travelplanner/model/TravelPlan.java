package com.travelplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlan {
    private String weatherInfo;
    private String suggestions;
    private List<DailySchedule> dailySchedules;
    private String contingencyPlan;
    private List<HotelSuggestion> hotels;
    private CostBreakdown costBreakdown;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailySchedule {
        private int dayNumber;
        private String date;
        private List<Activity> activities;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Activity {
        private String time;
        private String description;
        private String fee;
        private String imageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HotelSuggestion {
        private String name;
        private String description;
        private String priceRange;
        @JsonProperty("bookingUrl")
        private String bookingUrl;
        private String distanceFromTransportation;
        private String taxiCostFromTransportation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlightSuggestion {
        private String airline;
        private String flightNumber;
        private String estimatedPrice;
        private String bookingUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TrainSuggestion {
        private String trainName;
        private String estimatedPrice;
        private String bookingUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CostBreakdown {
        private String transportationType;
        private String nearestStation;
        private List<FlightSuggestion> flightSuggestions;
        private List<TrainSuggestion> trainSuggestions;
        private String totalDistance;
        private String transportationCost;
        private String taxiCost;
        private String accommodation;
        private String foodAndActivities;
        private String miscellaneous;
        private String total;
    }
}