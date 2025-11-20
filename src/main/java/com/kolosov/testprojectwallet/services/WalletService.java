package com.kolosov.testprojectwallet.services;

import com.kolosov.testprojectwallet.config.AppProperties;
import com.kolosov.testprojectwallet.da.mappers.WalletMapper;
import com.kolosov.testprojectwallet.da.models.Wallet;
import com.kolosov.testprojectwallet.da.repositories.WalletRepository;
import com.kolosov.testprojectwallet.dto.WalletDto;
import com.kolosov.testprojectwallet.errors.exceptions.ResourceNotFoundException;
import com.kolosov.testprojectwallet.errors.exceptions.WalletOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final AppProperties appProperties;

    @Transactional(readOnly = true)
    public WalletDto get(UUID id) {
        Optional<Wallet> walletOptional = walletRepository.findById(id);
        return walletOptional.map(walletMapper::convert).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public WalletDto deposit(UUID walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found wallet with ID: %s", walletId)));

        wallet.setAmount(wallet.getAmount() + amount);
        wallet = walletRepository.save(wallet);
        return walletMapper.convert(wallet);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public WalletDto withdraw(UUID walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found wallet with ID: %s", walletId)));

        if (amount > appProperties.getMaxWithdrawInOneTime()) {
            throw new WalletOperationException(String.format("You cant withdraw more than %.2f", appProperties.getMaxWithdrawInOneTime()));
        }

        if (wallet.getAmount() < amount) {
            throw new WalletOperationException(String.format("Not enough amount on wallet to withdraw %.2f", amount));
        }

        wallet.setAmount(wallet.getAmount() - amount);
        wallet = walletRepository.save(wallet);
        return walletMapper.convert(wallet);
    }
}
