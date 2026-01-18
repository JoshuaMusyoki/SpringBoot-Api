package com.comulynx.wallet.rest.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comulynx.wallet.rest.api.AppUtilities;
import com.comulynx.wallet.rest.api.model.Account;
import com.comulynx.wallet.rest.api.model.Customer;
import com.comulynx.wallet.rest.api.repository.AccountRepository;
import com.comulynx.wallet.rest.api.repository.CustomerRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private Gson gson = new Gson();

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AccountRepository accountRepository;
	@GetMapping("/")
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	/**
	 * Fix Customer Login functionality
	 * 
	 * Login
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> customerLogin(@RequestBody String request) {
		try {
			JsonObject response = new JsonObject();

			final JsonObject req = gson.fromJson(request, JsonObject.class);
			String customerId = req.get("customerId").getAsString();
			String customerPIN = req.get("pin").getAsString();

			// TODO : Add Customer login logic here. Login using customerId and
			// PIN
			// NB: We are using plain text password for testing Customer login
			// If customerId doesn't exists throw an error "Customer does not exist"
			// If password do not match throw an error "Invalid credentials"
			
			Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
			if (!customerOptional.isPresent()) {
				return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
			}
			
			Customer customer = customerOptional.get();
			if (!customer.getPin().equals(customerPIN)) {
				return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
			}

			//TODO : Return a JSON object with the following after successful login
			//Customer Name, Customer ID, email and Customer Account 
			
			Optional<Account> accountOptional = accountRepository.findAccountByCustomerId(customerId);
			String accountNo = accountOptional.map(Account::getAccountNo).orElse("N/A");

			response.addProperty("customerName", customer.getFirstName() + " " + customer.getLastName());
			response.addProperty("customerId", customer.getCustomerId());
			response.addProperty("email", customer.getEmail());
			response.addProperty("accountNo", accountNo);

			return ResponseEntity.status(200).body(gson.toJson(response));

		} catch (Exception ex) {
			logger.info("Exception {}", AppUtilities.getExceptionStacktrace(ex));
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 *  Add required logic
	 *  
	 *  Create Customer
	 *  
	 * @param customer
	 * @return
	 */
	@PostMapping("/")
	public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
		try {
			String customerPIN = customer.getPin();
			String email = customer.getEmail();
			
			// TODO : Add logic to Hash Customer PIN here
			// For now, keeping it plain text as per login logic comment, but ideally should be hashed.
			// customer.setPin(hash(customerPIN)); 
			
			//  : Add logic to check if Customer with provided email, or
			// customerId exists. If exists, throw a Customer with [?] exists
			// Exception.
			
			if (customerRepository.findByCustomerId(customer.getCustomerId()).isPresent()) {
				return new ResponseEntity<>("Customer with customerId " + customer.getCustomerId() + " exists", HttpStatus.CONFLICT);
			}
			
			if (customerRepository.findByEmail(email).isPresent()) {
				return new ResponseEntity<>("Customer with email " + email + " exists", HttpStatus.CONFLICT);
			}

			String accountNo = generateAccountNo(customer.getCustomerId());
			Account account = new Account();
			account.setCustomerId(customer.getCustomerId());
			account.setAccountNo(accountNo);
			account.setBalance(0.0);
			accountRepository.save(account);

			return ResponseEntity.ok().body(customerRepository.save(customer));
		} catch (Exception ex) {
			logger.info("Exception {}", AppUtilities.getExceptionStacktrace(ex));

			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 *  Add required functionality
	 *  
	 * generate a random but unique Account No (NB: Account No should be unique
	 * in your accounts table)
	 * 
	 */
	private String generateAccountNo(String customerId) {
		// TODO : Add logic here - generate a random but unique Account No (NB:
		// Account No should be unique in the accounts table)
		Random rand = new Random();
		String accountNo = "ACC" + (rand.nextInt(900000) + 100000);
		
		// Simple check for uniqueness (in a real app, this might need a loop or DB constraint handling)
		while (accountRepository.findAccountByAccountNo(accountNo).isPresent()) {
			accountNo = "ACC" + (rand.nextInt(900000) + 100000);
		}
		
		return accountNo;
	}
}
