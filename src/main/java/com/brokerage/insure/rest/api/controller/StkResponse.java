package com.brokerage.insure.rest.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StkResponse {
    private String checkoutRequestID;
    private String customerMessage;
    private String responseDescription;
}
