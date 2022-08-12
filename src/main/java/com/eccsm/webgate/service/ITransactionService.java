package com.eccsm.webgate.service;

import java.util.List;

import com.google.gson.JsonElement;

public interface ITransactionService {
	JsonElement saveTransaction(JsonElement transaction);

	void deleteTransaction(Long transactionId);

	List<JsonElement> getAllTransactionsOfUser(Long userId);
}
