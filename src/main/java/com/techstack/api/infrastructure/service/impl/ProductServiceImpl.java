package com.techstack.api.infrastructure.service.impl;

import com.techstack.api.domain.repository.ProductRepository;
import com.techstack.api.infrastructure.service.ProductService;
import com.techstack.api.payload.data.ProductData;
import com.techstack.api.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ApiResponse<List<ProductData>> getAll() {
        var products = productRepository.findAll();
        var data = products.stream().map(product -> ProductData.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .minimumOpeningBalance(product.getMinimumOpeningBalance())
                .maximumOpeningBalance(product.getMaximumOpeningBalance())
                .interestRate(product.getInterestRate())
                .currency(product.getCurrency())
                .type(product.getType().name())
                .build())
                .toList();
        return ApiResponse.ok("Products retrieved successfully", data);
    }
}
