package com.brokerage.insure.rest.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String method;      // e.g., "CARD", "BANK"
    private Double amount;
    private Long timestamp;
    private String quoteId;     // Optional: link it back to a specific quote
}
