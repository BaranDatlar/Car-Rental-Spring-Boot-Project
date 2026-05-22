package com.baran.rentacar.business.concretes;

import com.baran.rentacar.business.abstracts.AccountService;
import com.baran.rentacar.business.requests.CreateAccountRequest;
import com.baran.rentacar.business.responses.GetAccountResponse;
import com.baran.rentacar.business.rules.AccountBusinessRules;
import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.core.utilities.mappers.ModelMapperService;
import com.baran.rentacar.dataAccess.abstracts.AccountRepository;
import com.baran.rentacar.entities.concretes.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@AllArgsConstructor
@Service
public class AccountManager implements AccountService {

    private AccountRepository accountRepository;
    private AccountBusinessRules accountBusinessRules;
    private ModelMapperService modelMapperService;

    @Override
    public GetAccountResponse add(CreateAccountRequest createAccountRequest) {
        accountBusinessRules.checkIfAccountNumberExists(createAccountRequest.getAccountNumber());

        Account account = modelMapperService.forRequest().map(createAccountRequest, Account.class);
        account.setBalance(createAccountRequest.getInitialBalance());
        Account savedAccount = accountRepository.save(account);
        return modelMapperService.forRequest().map(savedAccount, GetAccountResponse.class);
    }

    @Override
    public GetAccountResponse getByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Account not found: " + accountNumber));
        return modelMapperService.forResponse().map(account, GetAccountResponse.class);
    }
}
