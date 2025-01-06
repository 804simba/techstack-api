package com.techstack.api.infrastructure.helper;

import com.techstack.api.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class AccountServiceHelper {
    private final AccountRepository accountRepository;

    public String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;
        boolean exists;

        do {
            StringBuilder builder = new StringBuilder("804");
            for (int i = 0; i < 7; i++) {
                builder.append(random.nextInt(10));
            }
            accountNumber = builder.toString();

            try {
                exists = accountRepository.existsByAccountNumber(accountNumber);
            } catch (Exception e) {
                exists = false;
            }
        } while (exists);

        return accountNumber;
    }
}
