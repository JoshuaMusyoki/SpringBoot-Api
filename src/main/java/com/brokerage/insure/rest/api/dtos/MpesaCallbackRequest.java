package com.brokerage.insure.rest.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MpesaCallbackRequest {
    @JsonProperty("Body")
    private CallbackBody body;

    @Data
    public static class CallbackBody {
        @JsonProperty("stkCallback")
        private StkCallback stkCallback;
    }

    @Data
    public static class StkCallback {
        private String MerchantRequestID;
        private String CheckoutRequestID;
        private Integer ResultCode; // 0 means Success
        private String ResultDesc;
        private CallbackMetadata CallbackMetadata;
    }

    @Data
    public static class CallbackMetadata {
        @JsonProperty("Item")
        private List<Item> item;
    }

    @Data
    public static class Item {
        private String Name;
        private Object Value;
    }
}
