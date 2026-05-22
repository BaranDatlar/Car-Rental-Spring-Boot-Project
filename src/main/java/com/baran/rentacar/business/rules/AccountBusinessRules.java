package com.baran.rentacar.business.rules;

import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.dataAccess.abstracts.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountBusinessRules {
    private final AccountRepository accountRepository;

    public void checkIfAccountNumberExists(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new BusinessException("Account number already exists");
        }
    }
}
