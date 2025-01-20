package com.techstack.api.infrastructure.service.impl;

import com.techstack.api.core.exception.AppException;
import com.techstack.api.domain.entity.Account;
import com.techstack.api.domain.repository.AccountRepository;
import com.techstack.api.domain.repository.AppUserRepository;
import com.techstack.api.domain.repository.ProductRepository;
import com.techstack.api.infrastructure.helper.AccountServiceHelper;
import com.techstack.api.payload.data.ProductData;
import com.techstack.api.payload.enums.AccountStatus;
import com.techstack.api.payload.request.account.CreateAccountRequest;
import com.techstack.api.payload.request.account.UpdateAccountRequest;
import com.techstack.api.payload.data.AccountData;
import com.techstack.api.payload.response.ApiResponse;
import com.techstack.api.payload.data.PaginatedData;
import com.techstack.api.infrastructure.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;
    private final AccountServiceHelper accountServiceHelper;
    private Integer counter = 0;

    @Transactional
    @Override
    public ApiResponse<AccountData> create(CreateAccountRequest request) {
        counter++;
        System.out.println("counter = " + counter);
        var existingUser = appUserRepository.findAppUserById(request.getUserId());
        if (existingUser.isEmpty()) {
            return ApiResponse.notFound("User not found");
        }
        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        boolean doesUserHaveAccountType = existingUser.get().getAccounts().stream()
                .anyMatch(account -> account.getType().equals(product.getType().name()));
        if (doesUserHaveAccountType) {
            return ApiResponse.badRequest("User already has an account of this type");
        }
        var account = Account.builder()
                .name(request.getName())
                .type(product.getType().name())
                .status(AccountStatus.INACTIVE)
                .accountNumber(accountServiceHelper.generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .user(existingUser.get())
                .product(product)
                .build();
        accountRepository.save(account);

        return ApiResponse.ok(
                "Account created successfully",
                AccountData.builder()
                        .name(account.getName())
                        .type(account.getType())
                        .accountNumber(account.getAccountNumber())
                        .build());
    }

    @Transactional
    @Override
    public ApiResponse<AccountData> update(Long customerId, UpdateAccountRequest request) {
        var account = accountRepository.findById(customerId)
                .orElseThrow(() -> new AppException("Account not found"));

        if (!request.getActivate()) {
            return ApiResponse.badRequest("Invalid activation request");
        }

        if (account.getStatus().equals(AccountStatus.ACTIVE)) {
            return ApiResponse.badRequest("Account is already active");
        }

        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);

        return ApiResponse.ok("Account updated successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<AccountData> get(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("Account not found"));

        return ApiResponse.ok("Fetched account successfully",
                AccountData.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .type(account.getType())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .status(account.getStatus().name())
                        .product(ProductData.builder()
                                .id(account.getProduct().getId())
                                .currency(account.getProduct().getCurrency())
                                .interestRate(account.getProduct().getInterestRate())
                                .build())
                        .build());
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<List<AccountData>> search(String keyword) {
        var accounts = accountRepository.searchAccounts(keyword);

        if (accounts.isEmpty()) {
            return ApiResponse.notFound("No accounts found");
        }

        var data = accounts
                .stream()
                .map(account -> AccountData.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .type(account.getType())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .status(account.getStatus().name())
                        .product(ProductData.builder()
                                .name(account.getProduct().getName())
                                .type(account.getProduct().getType().name())
                                .build())
                        .build())
                .toList();
        return ApiResponse.ok("Fetched accounts successfully",
                data);
    }

    @Override
    public ApiResponse<PaginatedData<AccountData>> getUserAccounts(Long userId, Integer pageNumber, Integer pageSize) {
        var pageable = PageRequest.ofSize(pageSize).withPage(pageNumber);
        var accounts = accountRepository.findAccountsByUserId(userId, pageable);

        if (accounts.isEmpty()) {
            return ApiResponse.notFound("No accounts found");
        }
        long size = accounts.getTotalElements();
        int page = accounts.getNumber() + 1;

        List<AccountData> data = accounts
                .stream()
                .map(account -> AccountData.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .type(account.getType())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .status(account.getStatus().name())
                        .product(ProductData.builder()
                                .id(account.getProduct().getId())
                                .currency(account.getProduct().getCurrency())
                                .build())
                        .build())
                .toList();

        var results = new PaginatedData<>(size, page, data);

        return ApiResponse.ok("Fetched accounts successfully", results);
    }

    @Override
    public ApiResponse<AccountData> delete(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("Account not found"));
        accountRepository.delete(account);
        return ApiResponse.ok("Account deleted successfully");
    }
}
