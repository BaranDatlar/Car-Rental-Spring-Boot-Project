package com.baran.rentacar.webApi.controllers;

import com.baran.rentacar.business.abstracts.AccountService;
import com.baran.rentacar.business.requests.CreateAccountRequest;
import com.baran.rentacar.business.responses.GetAccountResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<GetAccountResponse> add(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        GetAccountResponse response = accountService.add(createAccountRequest);
        URI location = URI.create("/api/v1/accounts/" + response.getAccountNumber());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<GetAccountResponse> getByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber));
    }
}
