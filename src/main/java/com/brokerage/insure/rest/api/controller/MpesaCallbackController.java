package com.brokerage.insure.rest.api.controller;

import com.brokerage.insure.rest.api.dtos.MpesaCallbackRequest;
import com.brokerage.insure.rest.api.dtos.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class MpesaCallbackController {

    // Simple thread-safe map to store results for polling
    // In production, use a Database (JPA) or Redis
    public static final Map<String, PaymentStatusResponse> transactionCache = new ConcurrentHashMap<>();

    @PostMapping("/callback")
    public ResponseEntity<String> handleMpesaCallback(@RequestBody MpesaCallbackRequest request) {
        var callback = request.getBody().getStkCallback();
        String checkoutId = callback.getCheckoutRequestID();

        log.info("Received M-Pesa Callback for CheckoutID: {}", checkoutId);

        if (callback.getResultCode() == 0) {
            // Extraction of Receipt Number from Metadata
            String receipt = callback.getCallbackMetadata().getItem().stream()
                    .filter(i -> i.getName().equals("MpesaReceiptNumber"))
                    .findFirst()
                    .map(i -> i.getValue().toString())
                    .orElse("UNKNOWN");

            log.info("Payment Successful! Receipt: {}", receipt);
            transactionCache.put(checkoutId, new PaymentStatusResponse(true, receipt, 0.0));
        } else {
            log.warn("Payment Failed: {}", callback.getResultDesc());
            transactionCache.put(checkoutId, new PaymentStatusResponse(false, "FAILED", 0.0));
        }

        // Safaricom expects a 200 OK response to stop retrying
        return ResponseEntity.ok("Success");
    }
}
