package com.comulynx.wallet.rest.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comulynx.wallet.rest.api.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCustomerId(String customerId);
	
	Optional<Customer> findByEmail(String email);

	// TODO : Implement the query and function below to delete a customer using Customer Id
	@Transactional
	@Modifying
	@Query("DELETE FROM Customer c WHERE c.customerId = ?1")
	int deleteCustomerByCustomerId(String customer_id);

	// TODO : Implement the query and function below to update customer firstName using Customer Id
	@Transactional
	@Modifying
	@Query("UPDATE Customer c SET c.firstName = ?1 WHERE c.customerId = ?2")
	int updateCustomerByCustomerId(String firstName, String customer_id);
	
	// TODO : Implement the query and function below and to return all customers whose Email contains  'gmail'
	@Query("SELECT c FROM Customer c WHERE c.email LIKE '%gmail%'")
	List<Customer> findAllCustomersWhoseEmailContainsGmail();
}
