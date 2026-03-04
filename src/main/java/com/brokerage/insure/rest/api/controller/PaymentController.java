package com.brokerage.insure.rest.api.controller;

import com.brokerage.insure.rest.api.dtos.PaymentRequest;
import com.brokerage.insure.rest.api.dtos.PaymentStatusResponse;
import com.brokerage.insure.rest.api.dtos.StkRequest;
import com.brokerage.insure.rest.api.service.MpesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final MpesaService mpesaService;

    @PostMapping("/stk-push")
    public ResponseEntity<StkResponse> triggerStk(@RequestBody StkRequest request) {
        // Logic to generate LNM Password and call Safaricom
        StkResponse response = mpesaService.sendStkPush(request);
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/status/{checkoutId}")
    public ResponseEntity<PaymentStatusResponse> getStatus(@PathVariable String checkoutId) {
        // Query your DB to see if Safaricom sent a callback for this ID
        PaymentStatusResponse status = mpesaService.getPaymentStatus(checkoutId);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/submit")
    public ResponseEntity<PaymentResponse> submitGeneral(@RequestBody PaymentRequest request) {
        // Handle Card/Bank logic
        return ResponseEntity.ok(new PaymentResponse("RE-" + System.currentTimeMillis(), "SUCCESS"));
    }
}