package com.eccsm.webgate.service;

import java.util.List;

import com.google.gson.JsonElement;

public interface IProductService {
	JsonElement saveProduct(JsonElement requestBody);

	void deleteProduct(Long productId);

	List<JsonElement> getAllProducts();
}
