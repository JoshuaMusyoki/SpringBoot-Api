package com.brokerage.insure.rest.api.service;

import com.brokerage.insure.rest.api.controller.MpesaCallbackController;
import com.brokerage.insure.rest.api.controller.StkResponse;
import com.brokerage.insure.rest.api.dtos.PaymentStatusResponse;
import com.brokerage.insure.rest.api.dtos.StkRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MpesaService {
    @Value("${mpesa.consumer.key}")
    private String consumerKey;

    @Value("${mpesa.consumer.secret}")
    private String consumerSecret;

    @Value("${mpesa.passkey}")
    private String passkey;

    @Value("${mpesa.shortcode}")
    private String shortcode;

    @Value("${mpesa.callback.url}")
    private String callbackUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Retryable(
            value = { org.springframework.web.client.HttpServerErrorException.class, java.lang.RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public StkResponse sendStkPush(StkRequest request) {
        String accessToken = getAccessToken();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String password = Base64.getEncoder().encodeToString((shortcode.trim() + passkey.trim() + timestamp).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // This body structure is strictly defined by Safaricom Daraja API
        Map<String, Object> body = getStringObjectMap(request, password, timestamp);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        log.info("Attempting STK Push for {}", request.getPhoneNumber());
        try {
            ResponseEntity<StkResponse> response = restTemplate.postForEntity(
                    "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest",
                    entity,
                    StkResponse.class
            );
            return response.getBody();

        } catch (Exception e) {
            log.error("STK Push failed: {}", e.getMessage());
            throw new RuntimeException("Could not initiate M-Pesa payment");
        }
    }

    @Recover
    public StkResponse recover(RuntimeException e, StkRequest request) {
        log.error("All 3 attempts failed. Safaricom Sandbox is likely down.");
        throw new RuntimeException("Safaricom system is currently unavailable. Please try again in 5 minutes.");
    }

    private @NonNull Map<String, Object> getStringObjectMap(StkRequest request, String password, String timestamp) {
        Map<String, Object> body = new HashMap<>();
        double amount = (request.getAmount() != null) ? request.getAmount() : 1.0;
        body.put("BusinessShortCode", shortcode);
        body.put("Password", password);
        body.put("Timestamp", timestamp);
        body.put("TransactionType", "CustomerPayBillOnline");
        body.put("Amount", Math.round(amount));
        body.put("PartyA", request.getPhoneNumber()); // Format: 2547xxxxxxxx
        body.put("PartyB", shortcode);
        body.put("PhoneNumber", request.getPhoneNumber());
        body.put("CallBackURL", callbackUrl);
        body.put("AccountReference", request.getAccountReference());
        body.put("TransactionDesc", "Insurance Quote Payment");
        return body;
    }

    private String getAccessToken() {
        String credentials = Base64.getEncoder().encodeToString((consumerKey + ":" + consumerSecret).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + credentials);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials",
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    public PaymentStatusResponse getPaymentStatus(String checkoutId) {
        // Check the cache populated by the CallbackController
        return MpesaCallbackController.transactionCache.getOrDefault(
                checkoutId,
                new PaymentStatusResponse(false, "PENDING", 0.0)
        );
    }
}
