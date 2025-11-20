package com.kolosov.testprojectwallet.da.mappers;

import com.kolosov.testprojectwallet.da.models.Wallet;
import com.kolosov.testprojectwallet.dto.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WalletMapper extends MapperInterface<Wallet, WalletDto> {
}
