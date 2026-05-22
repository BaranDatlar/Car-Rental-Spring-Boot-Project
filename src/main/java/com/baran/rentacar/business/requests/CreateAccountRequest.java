package com.baran.rentacar.business.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    @NotBlank
    @Size(min = 10, max = 34)
    private String accountNumber;
    @NotBlank
    @Size(min = 2, max = 255)
    private String ownerName;
    @NotNull @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal initialBalance;
    @NotBlank @Size(min = 3, max = 3)
    private String currency;
}
