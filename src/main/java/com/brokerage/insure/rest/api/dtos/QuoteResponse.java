package com.brokerage.insure.rest.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteResponse {
    private Double basePremium;
    private Double taxesAndLevies;
    private Double totalAmount;
    private String currency; // Usually "KES"
}
