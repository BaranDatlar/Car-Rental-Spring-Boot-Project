package com.baran.rentacar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountResponse {
    private Long id;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private Instant createdAt;
    private String createdBy;
}
