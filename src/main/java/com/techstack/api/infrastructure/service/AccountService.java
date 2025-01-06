package com.techstack.api.infrastructure.service;

import com.techstack.api.payload.request.account.CreateAccountRequest;
import com.techstack.api.payload.request.account.UpdateAccountRequest;
import com.techstack.api.payload.data.AccountData;
import com.techstack.api.payload.response.ApiResponse;
import com.techstack.api.payload.data.PaginatedData;

import java.util.List;

public interface AccountService {
    ApiResponse<AccountData> create(CreateAccountRequest request);
    ApiResponse<AccountData> update(Long customerId, UpdateAccountRequest request);
    ApiResponse<AccountData> get(Long customerId);
    ApiResponse<PaginatedData<AccountData>> getUserAccounts(Long customerId, Integer pageNumber, Integer pageSize);
    ApiResponse<List<AccountData>> search(String keyword);

    ApiResponse<AccountData> delete(Long accountId);
}
