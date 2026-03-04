package com.brokerage.insure.rest.api.dtos;

import lombok.Data;

@Data
public class StkRequest {
    private String phone;
    private Double amount;
    private String reference;
}
