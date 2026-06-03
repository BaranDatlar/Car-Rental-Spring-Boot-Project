package com.baran.rentacar.business.responses;

import com.baran.rentacar.entities.concretes.enums.TransferStatus;
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
public class GetTransferResponse {
    private String reference;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String currency;
    private TransferStatus status;
    private String description;
    private Instant createdAt;
    private String createdBy;
}
