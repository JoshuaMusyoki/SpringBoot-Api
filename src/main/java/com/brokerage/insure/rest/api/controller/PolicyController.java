package com.brokerage.insure.rest.api.controller;

import com.brokerage.insure.rest.api.model.Account;
import com.brokerage.insure.rest.api.model.CustomerPolicy;
import com.brokerage.insure.rest.api.model.InsuranceProduct;
import com.brokerage.insure.rest.api.repository.AccountRepository;
import com.brokerage.insure.rest.api.repository.CustomerPolicyRepository;
import com.brokerage.insure.rest.api.repository.InsuranceProductRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    @Autowired
    private InsuranceProductRepository productRepository;
    @Autowired
    private CustomerPolicyRepository customerPolicyRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Gson gson = new Gson();

    /**
     * LIST ALL PRODUCTS
     * UX: The "Shop" or "Explore" screen
     */
    @GetMapping("/products")
    public List<InsuranceProduct> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * BUY A POLICY
     * UX: The "Click to Buy" action
     */
    @PostMapping("/purchase")
    public ResponseEntity<?> purchasePolicy(@RequestBody String request) {
        try {
            JsonObject req = gson.fromJson(request, JsonObject.class);
            String customerId = req.get("customerId").getAsString();
            Long productId = req.get("productId").getAsLong();

            // 1. Get Product and Account details
            InsuranceProduct product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Account account = accountRepository.findAccountByCustomerId(customerId)
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            // 2. Check if user has enough money in their Reica Wallet
            if (account.getBalance() < product.getBasePremium()) {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Insufficient balance in wallet");
            }

            // 3. Deduct money from Wallet
            account.setBalance(account.getBalance() - product.getBasePremium());
            accountRepository.save(account);

            // 4. Create the Policy
            CustomerPolicy policy = new CustomerPolicy();
            policy.setCustomerId(customerId);
            policy.setProductId(productId);
            policy.setPolicyNumber("POL-" + System.currentTimeMillis());
            policy.setPremiumPaid(product.getBasePremium());
            policy.setStartDate(LocalDateTime.now());
            policy.setEndDate(LocalDateTime.now().plusYears(1)); // 1 year cover
            policy.setStatus("ACTIVE");

            customerPolicyRepository.save(policy);

            return ResponseEntity.ok("Policy purchased successfully! Policy No: " + policy.getPolicyNumber());

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * LIST CUSTOMER POLICIES
     * UX: "My Policies" screen
     */
    @GetMapping("/my-policies")
    public ResponseEntity<?> getMyPolicies(@RequestParam String customerId) {
        return ResponseEntity.ok(customerPolicyRepository.findByCustomerId(customerId));
    }
}
