package com.eccsm.webgate.request;

import java.util.List;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.*;

public interface IProductServiceRequest {
	@POST("/api/product")
	Call<JsonElement> saveProduct(@Body JsonElement requestBody);

	@DELETE("/api/product/{productId}")
	Call<Void> deleteProduct(@Path("productId") Long productId);

	@GET("/api/product")
	Call<List<JsonElement>> getAllProducts();
}
