package com.comulynx.wallet.rest.api.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import com.comulynx.wallet.rest.api.model.Account;
import com.comulynx.wallet.rest.api.repository.AccountRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private Gson gson = new Gson();

	@Autowired
	private AccountRepository accountRepository;

	@GetMapping("/")
	public List<Account> getAllAccount() {
		return accountRepository.findAll();
	}

	@PostMapping("/")
	public ResponseEntity<?> getAccountForCustomer(@RequestBody String request)throws ResourceNotFoundException{
		try {
			JsonObject response = new JsonObject();
			final JsonObject accountRequest = gson.fromJson(request, JsonObject.class);
			String customerId = accountRequest.get("customerId").getAsString();

			// TODO : Added logic to find Account by CustomerId->done
			Optional<Account> account = accountRepository.findAccountByCustomerId(customerId);
			if (account.isPresent()){
				response.addProperty("accountNo", account.get().getAccountNo() );
				response.addProperty("customerId", account.get().getCustomerId() );
				response.addProperty("balance", account.get().getBalance());
				response.addProperty("id", account.get().getId());

				//return new ResponseEntity<>(response , HttpStatus.OK);
				return ResponseEntity.ok().body(gson.toJson(response));
			}else {
				return new ResponseEntity<>("No Account found",HttpStatus.NOT_FOUND);
			}

		}catch ( Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/balance")
	public ResponseEntity<?> getAccountBalanceByCustomerIdAndAccountNo(@RequestBody String request)
			throws ResourceNotFoundException {
		try {
			JsonObject response = new JsonObject();

			final JsonObject balanceRequest = gson.fromJson(request, JsonObject.class);
			String customerId = balanceRequest.get("customerId").getAsString();
			String accountNo = balanceRequest.get("accountNo").getAsString();


			// TODO : Add logic to find Account balance by CustomerId->done
			Optional<Account> account = accountRepository.findAccountByCustomerIdAndAccountNo(customerId, accountNo);

			if (account.isPresent()){
				response.addProperty("balance", account.get().getBalance());
				response.addProperty("accountNo", account.get().getAccountNo());
				return ResponseEntity.ok().body(gson.toJson(response));
			}
			else {
				return new ResponseEntity<>("Account does not exist",HttpStatus.NOT_FOUND);
			}

		} catch (Exception ex) {
			logger.info("Exception {}", AppUtilities.getExceptionStacktrace(ex));

			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}