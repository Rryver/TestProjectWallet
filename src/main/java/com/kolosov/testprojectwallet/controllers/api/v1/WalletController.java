package com.kolosov.testprojectwallet.controllers.api.v1;

import com.kolosov.testprojectwallet.dto.WalletDto;
import com.kolosov.testprojectwallet.dto.requests.WalletOperationDto;
import com.kolosov.testprojectwallet.errors.exceptions.ResourceNotFoundException;
import com.kolosov.testprojectwallet.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = WalletController.REST_URL)
@RequiredArgsConstructor
public class WalletController extends AbstractApiController {
    public static final String REST_URL = AbstractApiController.BASE_REST_URL;

    private final WalletService walletService;

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<WalletDto> get(@PathVariable("WALLET_UUID") UUID uuid) {
        WalletDto walletDto = walletService.get(uuid);
        if (walletDto == null) {
            throw new ResourceNotFoundException(String.format("Not found wallet with ID: %s", uuid.toString()));
        }
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.OK)
    public void doOperation(@RequestBody @Valid WalletOperationDto operationDto) {
        switch (operationDto.getOperationType()) {
            case DEPOSIT -> walletService.deposit(operationDto.getId(), operationDto.getAmount());
            case WITHDRAW -> walletService.withdraw(operationDto.getId(), operationDto.getAmount());
        }
    }
}
