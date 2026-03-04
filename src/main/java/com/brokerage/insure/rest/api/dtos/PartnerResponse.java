package com.brokerage.insure.rest.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartnerResponse {
    private String id;
    private String name;
    private boolean isRecommended;
    private boolean isTopRated;
}
