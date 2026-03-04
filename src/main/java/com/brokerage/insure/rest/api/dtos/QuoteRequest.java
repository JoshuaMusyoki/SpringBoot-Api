package com.brokerage.insure.rest.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRequest {
    private String insuranceType; // e.g., "MOTOR", "HEALTH"
    private Double sumInsured;
    private Integer durationMonths;
}
