package com.brokerage.insure.rest.api.service;

import com.brokerage.insure.rest.api.dtos.QuoteRequest;
import com.brokerage.insure.rest.api.dtos.QuoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class QuoteService {
    public QuoteResponse calculate(QuoteRequest request) {
        double baseRate = switch (request.getInsuranceType().toUpperCase()) {
            case "MOTOR" -> 0.04; // 4%
            case "HEALTH" -> 0.025; // 2.5%
            default -> 0.03;
        };

        double basePremium = request.getSumInsured() * baseRate;
        double taxesAndLevies = basePremium * 0.15; // 15% combined levies
        double total = basePremium + taxesAndLevies + 40.0; // Adding fixed policy charge

        return new QuoteResponse(basePremium, taxesAndLevies, total, "KES");
    }
}

