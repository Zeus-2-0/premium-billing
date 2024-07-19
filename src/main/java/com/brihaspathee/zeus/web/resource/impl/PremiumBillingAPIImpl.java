package com.brihaspathee.zeus.web.resource.impl;

import com.brihaspathee.zeus.constants.ApiResponseConstants;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.AccountList;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.service.interfaces.AccountService;
import com.brihaspathee.zeus.service.interfaces.DataCleanUpService;
import com.brihaspathee.zeus.web.resource.interfaces.PremiumBillingAPI;
import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:54 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.web.resource.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PremiumBillingAPIImpl implements PremiumBillingAPI {

    /**
     * Account service instance
     */
    private final AccountService accountService;

    /**
     * Data clean up service instance
     */
    private final DataCleanUpService dataCleanUpService;

    /**
     * Clean up the transactional data from the database
     * @return
     */
    @Override
    public ResponseEntity<ZeusApiResponse<String>> cleanUp() {
        dataCleanUpService.deleteAll();
        ZeusApiResponse<String> apiResponse = ZeusApiResponse.<String>builder()
                .response("Transaction data from validation service deleted successfully")
                .statusCode(204)
                .status(HttpStatus.NO_CONTENT)
                .developerMessage(ApiResponseConstants.SUCCESS)
                .message(ApiResponseConstants.SUCCESS_REASON)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }

    /**
     * Create a payment for an enrollment span
     * @param premiumPaymentDto
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public ResponseEntity<ZeusApiResponse<PremiumPaymentDto>> createPayment(PremiumPaymentDto premiumPaymentDto)
            throws JsonProcessingException {
        log.info("Premium payment request: {}", premiumPaymentDto);
//        premiumPaymentService.createPremiumPayment(premiumPaymentDto);
        accountService.createPremiumPayment(premiumPaymentDto);
        return null;
    }

    /**
     * Get account by account number
     * @param accountNumber
     * @return
     */
    @Override
    public ResponseEntity<ZeusApiResponse<AccountList>> getAccountByNumber(String accountNumber) {
        AccountDto accountDto = accountService.getAccountByAccountNumber(accountNumber);
        AccountList accountList = AccountList.builder()
                .accountDtos(Set.of(accountDto))
                .build();
        ZeusApiResponse<AccountList> apiResponse = ZeusApiResponse.<AccountList>builder()
                .response(accountList)
                .timestamp(LocalDateTime.now(ZoneId.systemDefault()))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(ApiResponseConstants.SUCCESS)
                .developerMessage(ApiResponseConstants.SUCCESS_REASON)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
