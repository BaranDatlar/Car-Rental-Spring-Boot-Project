package com.baran.rentacar.business.abstracts;

import com.baran.rentacar.business.requests.CreateAccountRequest;
import com.baran.rentacar.business.responses.GetAccountResponse;


public interface AccountService {
    GetAccountResponse add(CreateAccountRequest createAccountRequest);
    GetAccountResponse getByAccountNumber(String accountNumber);
}
