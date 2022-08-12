package com.eccsm.webgate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eccsm.webgate.request.IProductServiceRequest;
import com.eccsm.webgate.utils.RetrofitUtils;
import com.google.gson.JsonElement;

@Service
public class ProductService implements IProductService {

	@Autowired
	private IProductServiceRequest productServiceRequest;

	@Override
	public JsonElement saveProduct(JsonElement requestBody) {
		return RetrofitUtils.executeInBlock(productServiceRequest.saveProduct(requestBody));
	}

	@Override
	public void deleteProduct(Long productId) {
		RetrofitUtils.executeInBlock(productServiceRequest.deleteProduct(productId));

	}

	@Override
	public List<JsonElement> getAllProducts() {
		return RetrofitUtils.executeInBlock(productServiceRequest.getAllProducts());
	}

}
