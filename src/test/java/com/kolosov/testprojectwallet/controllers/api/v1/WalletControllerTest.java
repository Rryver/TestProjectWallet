package com.kolosov.testprojectwallet.controllers.api.v1;

import com.kolosov.testprojectwallet.AbstractControllerTests;
import com.kolosov.testprojectwallet.dto.OperationType;
import com.kolosov.testprojectwallet.dto.WalletDto;
import com.kolosov.testprojectwallet.dto.requests.WalletOperationDto;
import com.kolosov.testprojectwallet.errors.exceptions.ResourceNotFoundException;
import com.kolosov.testprojectwallet.errors.exceptions.WalletOperationException;
import com.kolosov.testprojectwallet.services.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest extends AbstractControllerTests {

    private static final String WALLET_BY_ID_REST_URL = WalletController.REST_URL + "/wallets/";
    private static final String WALLET_DO_OPERATION_REST_URL = WalletController.REST_URL + "/wallet";

    @MockitoBean
    WalletService walletService;

    @Test
    void get() throws Exception {
        WalletDto expectedWalletDto = new WalletDto(UUID.fromString("8d1208fc-f401-496c-9cb8-483fef121234"), 1000.5);
        String expectedJson = jsonHelper.writeValue(expectedWalletDto);

        when(walletService.get(expectedWalletDto.getId())).thenReturn(expectedWalletDto);

        perform(MockMvcRequestBuilders.get(WALLET_BY_ID_REST_URL + "/" + expectedWalletDto.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));

        verify(walletService, times(1)).get(expectedWalletDto.getId());
    }

    @Test
    void get_when_notFound() throws Exception {
        String walletId = "8d1208fc-f401-496c-9cb8-483fef121236";
        perform(MockMvcRequestBuilders.get(WALLET_BY_ID_REST_URL + "/" + walletId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(walletService, times(1)).get(UUID.fromString(walletId));
    }

    @Test
    void get_when_invalid_UUID() throws Exception {
        String invalidUUID = "8d1208fc";
        perform(MockMvcRequestBuilders.get(WALLET_BY_ID_REST_URL + "/" + invalidUUID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(walletService, times(0)).get(any(UUID.class));
    }

    @Test
    void doOperation_deposit() throws Exception {
        WalletOperationDto walletOperationDto = new WalletOperationDto(
                UUID.fromString("8d1208fc-f401-496c-9cb8-483fef121234"),
                OperationType.DEPOSIT,
                1000.0
        );

        WalletDto walletDtoAfterOperation = new WalletDto(walletOperationDto.getId(), 2000.0);
        when(walletService.deposit(walletOperationDto.getId(), walletOperationDto.getAmount())).thenReturn(walletDtoAfterOperation);

        String requestBody = jsonHelper.writeValue(walletOperationDto);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(WALLET_DO_OPERATION_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

        verify(walletService, times(1)).deposit(walletOperationDto.getId(), walletOperationDto.getAmount());
    }

    @Test
    void doOperation_withdraw_when_amount_enough() throws Exception {
        WalletOperationDto walletOperationDto = new WalletOperationDto(
                UUID.fromString("8d1208fc-f401-496c-9cb8-483fef121234"),
                OperationType.WITHDRAW,
                1000.0
        );

        WalletDto walletDtoAfterOperation = new WalletDto(walletOperationDto.getId(), 2000.0);
        when(walletService.withdraw(walletOperationDto.getId(), walletOperationDto.getAmount())).thenReturn(walletDtoAfterOperation);

        String requestBody = jsonHelper.writeValue(walletOperationDto);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(WALLET_DO_OPERATION_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

        verify(walletService, times(1)).withdraw(walletOperationDto.getId(), walletOperationDto.getAmount());
    }

    @Test
    void doOperation_withdraw_when_amount_not_enough() throws Exception {
        WalletOperationDto walletOperationDto = new WalletOperationDto(
                UUID.fromString("8d1208fc-f401-496c-9cb8-483fef121234"),
                OperationType.WITHDRAW,
                1000.0
        );

        when(walletService.withdraw(walletOperationDto.getId(), walletOperationDto.getAmount())).thenThrow(WalletOperationException.class);

        String requestBody = jsonHelper.writeValue(walletOperationDto);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(WALLET_DO_OPERATION_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        perform(builder).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        verify(walletService, times(1)).withdraw(walletOperationDto.getId(), walletOperationDto.getAmount());
    }

    @Test
    void doOperation_when_wallet_not_found() throws Exception {
        WalletOperationDto walletOperationDto = new WalletOperationDto(
                UUID.fromString("8d1208fc-f401-496c-9cb8-483fef121234"),
                OperationType.DEPOSIT,
                1000.0
        );

        when(walletService.deposit(walletOperationDto.getId(), walletOperationDto.getAmount())).thenThrow(ResourceNotFoundException.class);

        String requestBody = jsonHelper.writeValue(walletOperationDto);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(WALLET_DO_OPERATION_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        perform(builder).andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(walletService, times(1)).deposit(walletOperationDto.getId(), walletOperationDto.getAmount());
    }

    @Test
    void doOperation_when_bad_request() throws Exception {
        String requestBody = "{}";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(WALLET_DO_OPERATION_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        perform(builder).andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(walletService, times(0)).deposit(any(UUID.class), anyDouble());
        verify(walletService, times(0)).withdraw(any(UUID.class), anyDouble());
    }
}