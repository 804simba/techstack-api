package com.techstack.api.infrastructure.api;

import com.techstack.api.payload.data.AccountData;
import com.techstack.api.payload.data.PaginatedData;
import com.techstack.api.payload.request.account.CreateAccountRequest;
import com.techstack.api.infrastructure.service.AccountService;
import com.techstack.api.commons.ApiRoute;
import com.techstack.api.payload.request.account.UpdateAccountRequest;
import com.techstack.api.payload.request.search.SearchRequest;
import com.techstack.api.payload.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Accounts", description = "Accounts API")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoute.Accounts.BASE)
public class AccountController {
    private final AccountService accountService;

    @PostMapping(ApiRoute.Accounts.NEW)
    public ApiResponse<AccountData> create(@RequestBody @Valid CreateAccountRequest request) {
        return accountService.create(request);
    }

    @PutMapping(ApiRoute.Accounts.ACTIVATE)
    public ApiResponse<AccountData> activate(@PathVariable(value = "accountId") Long userId, @RequestBody UpdateAccountRequest request) {
        return accountService.update(userId, request);
    }

    @GetMapping(ApiRoute.Accounts.ACCOUNT_INFORMATION)
    public ApiResponse<AccountData> get(@PathVariable(value = "accountId") Long accountId) {
        return accountService.get(accountId);
    }

    @PostMapping(ApiRoute.Accounts.SEARCH)
    public ApiResponse<List<AccountData>> search(@RequestBody @Valid SearchRequest request) {
        return accountService.search(request.getKeyword());
    }

    @GetMapping(ApiRoute.Accounts.GET_ACCOUNTS)
    public ApiResponse<PaginatedData<AccountData>> getUserAccounts(@PathVariable(value = "userId") Long customerId,
                                                                         @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                                                         @RequestParam(value = "size", defaultValue = "20") Integer pageSize) {
        return accountService.getUserAccounts(customerId, pageNumber, pageSize);
    }

    @DeleteMapping(ApiRoute.Accounts.DELETE_ACCOUNT)
    public ApiResponse<AccountData> delete(@PathVariable(value = "accountId") Long accountId) {
        return accountService.delete(accountId);
    }
}
