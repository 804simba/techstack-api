package com.techstack.api.core.seeder;

import com.techstack.api.domain.entity.Product;
import com.techstack.api.domain.repository.ProductRepository;
import com.techstack.api.payload.enums.ProductType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    @Override
    public void run(String... args) throws Exception {
        log.info("Running ProductSeeder...");
        List<Product> products = List.of(
                Product.builder()
                        .name("NGN Current Account")
                        .description("Current Account")
                        .minimumOpeningBalance(BigDecimal.valueOf(20000))
                        .maximumOpeningBalance(BigDecimal.valueOf(99999))
                        .interestRate(BigDecimal.ZERO)
                        .currency("NGN")
                        .type(ProductType.CURRENT_ACCOUNT)
                        .build(),
                Product.builder()
                        .name("NGN Savings Account")
                        .description("Savings Account")
                        .minimumOpeningBalance(BigDecimal.ZERO)
                        .maximumOpeningBalance(BigDecimal.valueOf(19999))
                        .interestRate(BigDecimal.ZERO)
                        .currency("NGN")
                        .type(ProductType.SAVINGS_ACCOUNT)
                        .build(),
                Product.builder()
                        .name("NGN Fixed Deposit")
                        .description("Fixed Deposit")
                        .minimumOpeningBalance(BigDecimal.valueOf(100000))
                        .maximumOpeningBalance(BigDecimal.valueOf(999999))
                        .interestRate(BigDecimal.ZERO)
                        .currency("NGN")
                        .type(ProductType.FIXED_DEPOSIT)
                        .build(),
                Product.builder()
                        .name("USD Savings Account")
                        .description("Savings Account")
                        .minimumOpeningBalance(BigDecimal.ZERO)
                        .maximumOpeningBalance(BigDecimal.valueOf(19999))
                        .interestRate(BigDecimal.valueOf(0.05))
                        .currency("USD")
                        .type(ProductType.SAVINGS_ACCOUNT)
                        .build(),
                Product.builder()
                        .name("USD Fixed Deposit")
                        .description("Fixed Deposit")
                        .minimumOpeningBalance(BigDecimal.valueOf(100000))
                        .maximumOpeningBalance(BigDecimal.valueOf(999999))
                        .interestRate(BigDecimal.valueOf(0.05))
                        .currency("USD")
                        .type(ProductType.FIXED_DEPOSIT)
                        .build(),
                Product.builder()
                        .name("GBP Savings Account")
                        .description("Savings Account")
                        .minimumOpeningBalance(BigDecimal.ZERO)
                        .maximumOpeningBalance(BigDecimal.valueOf(19999))
                        .interestRate(BigDecimal.valueOf(0.1))
                        .currency("GBP")
                        .type(ProductType.SAVINGS_ACCOUNT)
                        .build(),
                Product.builder()
                        .name("GBP Fixed Deposit")
                        .description("Fixed Deposit")
                        .minimumOpeningBalance(BigDecimal.valueOf(100000))
                        .maximumOpeningBalance(BigDecimal.valueOf(999999))
                        .interestRate(BigDecimal.valueOf(0.1))
                        .currency("GBP")
                        .type(ProductType.FIXED_DEPOSIT)
                        .build(),
                Product.builder()
                        .name("NGN Early Savers Savings Account")
                        .description("Savings Account")
                        .minimumOpeningBalance(BigDecimal.ZERO)
                        .maximumOpeningBalance(BigDecimal.valueOf(19999))
                        .interestRate(BigDecimal.valueOf(0.15))
                        .currency("NGN")
                        .type(ProductType.SAVINGS_ACCOUNT)
                        .build(),
                Product.builder()
                        .name("NGN Premium Fixed Deposit")
                        .description("Fixed Deposit")
                        .minimumOpeningBalance(BigDecimal.valueOf(100000))
                        .maximumOpeningBalance(BigDecimal.valueOf(999999))
                        .interestRate(BigDecimal.valueOf(0.15))
                        .currency("NGN")
                        .type(ProductType.FIXED_DEPOSIT)
                        .build()
        );
        productRepository.saveAll(products);
        log.info("ProductSeeder completed successfully");
    }
}
