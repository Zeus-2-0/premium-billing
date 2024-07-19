package com.brihaspathee.zeus.service.impl;

import com.brihaspathee.zeus.broker.message.request.BillingUpdateRequest;
import com.brihaspathee.zeus.broker.message.response.BillingUpdateResponse;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.service.interfaces.AccountService;
import com.brihaspathee.zeus.service.interfaces.BillingUpdateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 4:49 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BillingUpdateProcessorImpl implements BillingUpdateProcessor {

    /**
     * The instance of the account service
     */
    private final AccountService accountService;

    /**
     * Process the billing update requests received
     * @param billingUpdateRequest
     */
    @Override
    public Mono<BillingUpdateResponse> processBillingUpdates(BillingUpdateRequest billingUpdateRequest) {
        log.info("Inside the billing update processor");
        AccountDto accountDto = billingUpdateRequest.getAccountDto();
        log.info("Account dto received for update:{}", accountDto);
        accountService.updateAccount(accountDto);
        BillingUpdateResponse billingUpdateResponse = BillingUpdateResponse.builder()
                .responseCode("1001")
                .responseMessage("Successfully Updated the billing details")
                .build();
        return Mono.just(billingUpdateResponse);
    }
}
