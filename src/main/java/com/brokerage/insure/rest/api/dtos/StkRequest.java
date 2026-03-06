package com.brokerage.insure.rest.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.annotation.AliasFor;

@Data
public class StkRequest {
    @JsonProperty("BusinessShortCode")
    private String businessShortCode;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("TransactionType")
    private String transactionType = "CustomerPayBillOnline";

    @JsonProperty("Amount")
    @JsonAlias("amount")
    private Integer amount; // Use Integer, Safaricom sandbox can be picky with Doubles

    @JsonProperty("PartyA")
    private String partyA; // The phone sending money

    @JsonProperty("PartyB")
    private String partyB; // The shortcode receiving money

    @JsonProperty("PhoneNumber")
    @JsonAlias("phone")
    private String phoneNumber; // Same as PartyA

    @JsonProperty("CallBackURL")
    private String callBackURL;

    @JsonProperty("AccountReference")
    @JsonAlias("ref")
    private String accountReference;

    @JsonProperty("TransactionDesc")
    private String transactionDesc;
}
