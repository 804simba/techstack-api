package com.techstack.api.infrastructure.service;

import com.techstack.api.payload.data.ProductData;
import com.techstack.api.payload.response.ApiResponse;

import java.util.List;

public interface ProductService {
    ApiResponse<List<ProductData>> getAll();
}
