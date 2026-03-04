package com.brokerage.insure.rest.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentStatusResponse {
    private boolean isCompleted;
    private String receipt;
    private Double amount;
}
