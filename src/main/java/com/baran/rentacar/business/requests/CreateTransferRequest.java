package com.baran.rentacar.business.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferRequest
{
    @NotBlank @Size(min = 10, max = 34)
    private String fromAccountNumber;
    @NotBlank @Size(min = 10, max = 34)
    private String toAccountNumber;
    @NotNull @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;
    @NotBlank @Size(min = 3, max = 3)
    private String currency;
    @Size(max=255)
    private String description;
}
