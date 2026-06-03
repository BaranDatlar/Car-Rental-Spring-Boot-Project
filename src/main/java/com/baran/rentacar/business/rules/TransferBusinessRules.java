package com.baran.rentacar.business.rules;

import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.dataAccess.abstracts.AccountRepository;
import com.baran.rentacar.entities.concretes.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransferBusinessRules {
    private final AccountRepository accountRepository;

    public Account accountMustExist(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Account number not found " + accountNumber));
    }

    public void accountsMustBeDifferent(String fromAccountNumber, String toAccountNumber) {
        if(fromAccountNumber.equals(toAccountNumber)) throw new BusinessException("Cannot transfer to same account");
    }

    public void currenciesMustMatch(String fromCurrency, String toCurrency, String requestedCurrency) {
        if(!requestedCurrency.equals(fromCurrency) || !requestedCurrency.equals(toCurrency)) throw new BusinessException("Currencies do not match");
    }

    public void balanceMustBeSufficient(Account fromAccount, BigDecimal amount){
        if(fromAccount.getBalance().compareTo(amount) < 0) throw new BusinessException("Cannot transfer to more than balance");
    }


}
