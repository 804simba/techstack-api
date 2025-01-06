package com.techstack.api.infrastructure.service.impl;

import com.techstack.api.core.exception.AppException;
import com.techstack.api.domain.entity.Account;
import com.techstack.api.domain.entity.Address;
import com.techstack.api.domain.entity.AppUser;
import com.techstack.api.domain.entity.Product;
import com.techstack.api.domain.repository.AccountRepository;
import com.techstack.api.domain.repository.AppUserRepository;
import com.techstack.api.domain.repository.ProductRepository;
import com.techstack.api.infrastructure.helper.AccountServiceHelper;
import com.techstack.api.payload.data.*;
import com.techstack.api.payload.request.account.CreateAccountRequest;
import com.techstack.api.payload.request.account.UpdateAccountRequest;
import com.techstack.api.payload.request.user.CreateAppUserRequest;
import com.techstack.api.payload.response.ApiResponse;
import org.junit.jupiter.api.AfterEach;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountServiceHelper accountServiceHelper;
    @Mock
    private Account account;
    @Mock
    private AccountServiceImpl accountService;
    @InjectMocks
    private AppUserServiceImpl appUserService;

    private CreateAppUserRequest createUserRequest;
    private AppUser appUser;

    private CreateAccountRequest createRequest;
    private UpdateAccountRequest updateRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        // Setup test data
        Address address = Address.builder()
                .street("123 Test St")
                .city("Test City")
                .state("Test State")
                .country("Test Country")
                .zipCode("12345")
                .primary(true)
                .build();

        createUserRequest = CreateAppUserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .phoneNumber("+1234567890")
                .address(AddressData.builder()
                        .street("123 Test St")
                        .city("Test City")
                        .state("Test State")
                        .country("Test Country")
                        .zipCode("12345")
                        .build())
                .build();

        Set<Address> addresses = new HashSet<>();
        addresses.add(address);

        appUser = AppUser.builder()
                .name("Test User")
                .email("test@example.com")
                .phoneNumber("+1234567890")
                .addresses(addresses)
                .build();
        appUser.setId(1L);
    }

    @Test
    @DisplayName("Should create user successfully when user doesn't exist")
    void createUser_Success() {
        // Arrange
        when(appUserRepository.findAppUserByEmailOrPhoneNumber(
                createUserRequest.getEmail(),
                createUserRequest.getPhoneNumber()))
                .thenReturn(Optional.empty());

        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

        // Act
        ApiResponse<UserData> response = appUserService.create(createUserRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("User created successfully", response.getMessage());
        assertEquals("200", response.getCode());

        verify(appUserRepository).findAppUserByEmailOrPhoneNumber(
                createUserRequest.getEmail(),
                createUserRequest.getPhoneNumber());
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should return error when user already exists")
    void createUser_UserExists() {
        // Arrange
        when(appUserRepository.findAppUserByEmailOrPhoneNumber(
                createUserRequest.getEmail(),
                createUserRequest.getPhoneNumber()))
                .thenReturn(Optional.of(appUser));

        // Act
        ApiResponse<UserData> response = appUserService.create(createUserRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("User already exists", response.getMessage());
        assertEquals("400", response.getCode());

        verify(appUserRepository).findAppUserByEmailOrPhoneNumber(
                createUserRequest.getEmail(),
                createUserRequest.getPhoneNumber());
        verify(appUserRepository, never()).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should return all users successfully")
    void getAll_Success() {
        // Arrange
        List<AppUser> users = List.of(appUser);
        Page<AppUser> page = new PageImpl<>(users);

        when(appUserRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        ApiResponse<List<UserData>> response = appUserService.getAll(0, 10);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Users retrieved successfully", response.getMessage());
        assertEquals("200", response.getCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        UserData userData = response.getBody().get(0);
        assertEquals(appUser.getName(), userData.getName());
        assertEquals(appUser.getEmail(), userData.getEmail());
        assertEquals(appUser.getPhoneNumber(), userData.getPhoneNumber());

        verify(appUserRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return not found when no users exist")
    void getAll_NoUsers() {
        // Arrange
        Page<AppUser> emptyPage = new PageImpl<>(Collections.emptyList());
        when(appUserRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ApiResponse<List<UserData>> response = appUserService.getAll(0, 10);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("No users found", response.getMessage());
        assertEquals("404", response.getCode());

        verify(appUserRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle invalid page parameters")
    void getAll_InvalidPageParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> appUserService.getAll(-1, 10));

        assertThrows(IllegalArgumentException.class,
                () -> appUserService.getAll(0, 0));

        verify(appUserRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle repository exception")
    void createUser_RepositoryException() {
        // Arrange
        when(appUserRepository.findAppUserByEmailOrPhoneNumber(
                createUserRequest.getEmail(),
                createUserRequest.getPhoneNumber()))
                .thenReturn(Optional.empty());

        when(appUserRepository.save(any(AppUser.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> appUserService.create(createUserRequest));

        verify(appUserRepository).findAppUserByEmailOrPhoneNumber(
                createUserRequest.getEmail(),
                createUserRequest.getPhoneNumber());
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should fail to create account when user not found")
    void createAccount_UserNotFound() {
        // Arrange
        when(appUserRepository.findAppUserById(1L)).thenReturn(Optional.empty());

        // Act
        ApiResponse<AccountData> response = accountService.create(createRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getMessage());
        assertEquals("404", response.getCode());

        verify(appUserRepository).findAppUserById(1L);
        verify(productRepository, never()).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should fail to create account when product not found")
    void createAccount_ProductNotFound() {
        // Arrange
        when(appUserRepository.findAppUserById(1L)).thenReturn(Optional.of(appUser));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountService.create(createRequest));

        verify(appUserRepository).findAppUserById(1L);
        verify(productRepository).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should fail to update account when not found")
    void updateAccount_NotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppException.class, () -> accountService.update(1L, updateRequest));

        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should fail to update account with invalid activation request")
    void updateAccount_InvalidActivation() {
        // Arrange
        UpdateAccountRequest invalidRequest = UpdateAccountRequest.builder()
                .activate(false)
                .build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        ApiResponse<AccountData> response = accountService.update(1L, invalidRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Invalid activation request", response.getMessage());
        assertEquals("400", response.getCode());

        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should fail to get account when not found")
    void getAccount_NotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppException.class, () -> accountService.get(1L));

        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return not found when search returns no results")
    void searchAccounts_NoResults() {
        // Arrange
        when(accountRepository.searchAccounts(anyString())).thenReturn(Collections.emptyList());

        // Act
        ApiResponse<List<AccountData>> response = accountService.search("nonexistent");

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("No accounts found", response.getMessage());
        assertEquals("404", response.getCode());

        verify(accountRepository).searchAccounts(anyString());
    }

    @Test
    @DisplayName("Should return not found when user has no accounts")
    void getUserAccounts_NoAccounts() {
        // Arrange
        Page<Account> emptyPage = new PageImpl<>(Collections.emptyList());
        when(accountRepository.findAccountsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        ApiResponse<PaginatedData<AccountData>> response =
                accountService.getUserAccounts(1L, 0, 10);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("No accounts found", response.getMessage());
        assertEquals("404", response.getCode());

        verify(accountRepository).findAccountsByUserId(eq(1L), any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle invalid pagination parameters")
    void getUserAccounts_InvalidPagination() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.getUserAccounts(1L, -1, 10));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.getUserAccounts(1L, 0, 0));

        verify(accountRepository, never()).findAccountsByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should map account details correctly")
    void getAccount_CorrectMapping() {
        // Arrange
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.getProduct().setInterestRate(BigDecimal.valueOf(2.5));
        account.getProduct().setCurrency("USD");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        ApiResponse<AccountData> response = accountService.get(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        AccountData accountData = response.getBody();
        assertNotNull(accountData);
        assertEquals(account.getId(), accountData.getId());
        assertEquals(account.getName(), accountData.getName());
        assertEquals(account.getType(), accountData.getType());
        assertEquals(account.getAccountNumber(), accountData.getAccountNumber());
        assertEquals(account.getBalance(), accountData.getBalance());
        assertEquals(account.getStatus().name(), accountData.getStatus());

        ProductData productData = accountData.getProduct();
        assertNotNull(productData);
        assertEquals(account.getProduct().getId(), productData.getId());
        assertEquals(account.getProduct().getCurrency(), productData.getCurrency());
        assertEquals(account.getProduct().getInterestRate(), productData.getInterestRate());

        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("Should handle repository exception during save")
    void createAccount_RepositoryException() {
        // Arrange
        when(appUserRepository.findAppUserById(1L)).thenReturn(Optional.of(appUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(accountRepository.save(any(Account.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> accountService.create(createRequest));

        verify(appUserRepository).findAppUserById(1L);
        verify(productRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should validate required fields in create request")
    void createAccount_ValidateRequest() {
        // Arrange
        CreateAccountRequest invalidRequest = CreateAccountRequest.builder().build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> accountService.create(invalidRequest));

        verify(appUserRepository, never()).findAppUserById(anyLong());
        verify(productRepository, never()).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
    }
}