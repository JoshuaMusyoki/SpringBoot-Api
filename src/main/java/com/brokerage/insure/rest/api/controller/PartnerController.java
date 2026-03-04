package com.brokerage.insure.rest.api.controller;

import com.brokerage.insure.rest.api.dtos.PartnerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
public class PartnerController {
    @GetMapping
    public ResponseEntity<List<PartnerResponse>> getPartners() {
       // Mock Data
        List<PartnerResponse> partners = List.of(
                new PartnerResponse("1", "Jubilee Insurance", true, false),
                new PartnerResponse("2", "APA Insurance", false, true),
                new PartnerResponse("3", "Britam Insurance", false, false)
        );
        return ResponseEntity.ok(partners);
    }
}
