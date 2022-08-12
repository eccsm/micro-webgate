package com.eccsm.webgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eccsm.webgate.security.UserPrincipal;
import com.eccsm.webgate.service.ITransactionService;
import com.google.gson.JsonElement;

@RestController
@RequestMapping("gateway/transaction")
public class TransactionController {
	@Autowired
	private ITransactionService transactionService;

	@PostMapping
	public ResponseEntity<?> saveTransaction(@RequestBody JsonElement transaction) {
		return ResponseEntity.ok(transactionService.saveTransaction(transaction));
	}

	@DeleteMapping("{transactionId}")
	public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {
		transactionService.deleteTransaction(transactionId);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getAllTransactionsOfAuthorizedUser(@AuthenticationPrincipal UserPrincipal principal) {
		return ResponseEntity.ok(transactionService.getAllTransactionsOfUser(principal.getId()));
	}
}
