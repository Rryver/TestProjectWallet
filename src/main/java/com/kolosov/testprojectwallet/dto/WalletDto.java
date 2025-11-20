package com.kolosov.testprojectwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private UUID id;

    @DecimalMin("0.0")
    private Double amount;
}
