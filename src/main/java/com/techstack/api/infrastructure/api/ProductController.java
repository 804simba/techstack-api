package com.techstack.api.infrastructure.api;

import com.techstack.api.commons.ApiRoute;
import com.techstack.api.infrastructure.service.ProductService;
import com.techstack.api.payload.data.ProductData;
import com.techstack.api.payload.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Products", description = "Products API")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoute.Products.BASE)
public class ProductController {
    private final ProductService productService;

    @GetMapping(ApiRoute.Products.ALL)
    public ApiResponse<List<ProductData>> getAll() {
        return productService.getAll();
    }
}
