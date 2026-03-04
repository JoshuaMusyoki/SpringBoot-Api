package com.brokerage.insure.rest.api.controller;

import com.brokerage.insure.rest.api.dtos.QuoteRequest;
import com.brokerage.insure.rest.api.dtos.QuoteResponse;
import com.brokerage.insure.rest.api.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insurance")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    @PostMapping("/calculate-quote")
    public ResponseEntity<QuoteResponse> generateQuote(@RequestBody QuoteRequest request) {
        return ResponseEntity.ok(quoteService.calculate(request));
    }
}
