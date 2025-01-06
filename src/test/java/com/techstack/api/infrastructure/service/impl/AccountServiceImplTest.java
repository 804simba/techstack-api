package com.techstack.api.infrastructure.service.impl;

import com.techstack.api.core.exception.AppException;
import com.techstack.api.domain.entity.Account;
import com.techstack.api.domain.entity.AppUser;
import com.techstack.api.domain.entity.Product;
import com.techstack.api.domain.repository.AccountRepository;
import com.techstack.api.domain.repository.AppUserRepository;
import com.techstack.api.domain.repository.ProductRepository;
import com.techstack.api.infrastructure.helper.AccountServiceHelper;
import com.techstack.api.payload.data.AccountData;
import com.techstack.api.payload.data.PaginatedData;
import com.techstack.api.payload.enums.AccountStatus;
import com.techstack.api.payload.enums.ProductType;
import com.techstack.api.payload.request.account.CreateAccountRequest;
import com.techstack.api.payload.request.account.UpdateAccountRequest;
import com.techstack.api.payload.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AccountServiceHelper accountServiceHelper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AppUser user;
    private Product product;
    private Account account;
    private CreateAccountRequest createRequest;
    private UpdateAccountRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        user = AppUser.builder()
                .name("Test User")
                .accounts(new HashSet<>())
                .build();
        user.setId(1L);

        product = Product.builder()
                .name("Savings Account")
                .type(ProductType.SAVINGS_ACCOUNT)
                .currency("USD")
                .interestRate(BigDecimal.valueOf(2.5))
                .build();
        product.setId(1L);

        account = Account.builder()
                .name("Test Account")
                .type(ProductType.SAVINGS_ACCOUNT.name())
                .status(AccountStatus.INACTIVE)
                .accountNumber("8041234567")
                .balance(BigDecimal.ZERO)
                .user(user)
                .product(product)
                .build();
        account.setId(1L);

        createRequest = CreateAccountRequest.builder()
                .userId(1L)
                .productId(1L)
                .name("Test Account")
                .build();

        updateRequest = UpdateAccountRequest.builder()
                .activate(true)
                .build();
    }

    @Test
    @DisplayName("Should create account successfully")
    void createAccount_Success() {
        // Arrange
        when(appUserRepository.findAppUserById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(accountServiceHelper.generateAccountNumber()).thenReturn("8041234567");
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        ApiResponse<AccountData> response = accountService.create(createRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Account created successfully", response.getMessage());
        assertNotNull(response.getBody());
        assertEquals(account.getName(), response.getBody().getName());
        assertEquals(account.getType(), response.getBody().getType());

        verify(appUserRepository).findAppUserById(1L);
        verify(productRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should fail when user already has account type")
    void createAccount_UserHasAccountType() {
        // Arrange
        user.getAccounts().add(account);
        when(appUserRepository.findAppUserById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ApiResponse<AccountData> response = accountService.create(createRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("User already has an account of this type", response.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should update account status successfully")
    void updateAccount_Success() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        ApiResponse<AccountData> response = accountService.update(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Account updated successfully", response.getMessage());

        verify(accountRepository).findById(1L);
        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("Should fail to update already active account")
    void updateAccount_AlreadyActive() {
        // Arrange
        account.setStatus(AccountStatus.ACTIVE);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        ApiResponse<AccountData> response = accountService.update(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Account is already active", response.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should get account successfully")
    void getAccount_Success() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        ApiResponse<AccountData> response = accountService.get(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Fetched account successfully", response.getMessage());
        assertNotNull(response.getBody());
        assertEquals(account.getName(), response.getBody().getName());
        assertEquals(account.getType(), response.getBody().getType());
        assertEquals(account.getAccountNumber(), response.getBody().getAccountNumber());

        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("Should search accounts successfully")
    void searchAccounts_Success() {
        // Arrange
        List<Account> accounts = List.of(account);
        when(accountRepository.searchAccounts(anyString())).thenReturn(accounts);

        // Act
        ApiResponse<List<AccountData>> response = accountService.search("test");

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Fetched accounts successfully", response.getMessage());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(accountRepository).searchAccounts(anyString());
    }

    @Test
    @DisplayName("Should get user accounts with pagination")
    void getUserAccounts_Success() {
        // Arrange
        List<Account> accounts = List.of(account);
        Page<Account> page = new PageImpl<>(accounts);
        when(accountRepository.findAccountsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        // Act
        ApiResponse<PaginatedData<AccountData>> response =
                accountService.getUserAccounts(1L, 0, 10);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Fetched accounts successfully", response.getMessage());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(1, response.getBody().getTotal());

        verify(accountRepository).findAccountsByUserId(eq(1L), any(Pageable.class));
    }

    @Test
    @DisplayName("Should delete account successfully")
    void deleteAccount_Success() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        // Act
        ApiResponse<AccountData> response = accountService.delete(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Account deleted successfully", response.getMessage());

        verify(accountRepository).findById(1L);
        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("Should throw exception when account not found")
    void deleteAccount_NotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppException.class, () -> accountService.delete(1L));
        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).delete(any(Account.class));
    }
}