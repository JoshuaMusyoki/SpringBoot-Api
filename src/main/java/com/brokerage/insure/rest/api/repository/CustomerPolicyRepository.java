package com.brokerage.insure.rest.api.repository;

import com.brokerage.insure.rest.api.model.Customer;
import com.brokerage.insure.rest.api.model.CustomerPolicy;
import com.brokerage.insure.rest.api.model.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPolicyRepository extends JpaRepository<CustomerPolicy, Long> {
    List<CustomerPolicy> findByCustomerId(String customerId);
}
