package com.kolosov.testprojectwallet.dto.requests;

import com.kolosov.testprojectwallet.dto.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletOperationDto {
    @NotNull
    private UUID id;

    @NotNull
    private OperationType operationType;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double amount;
}
