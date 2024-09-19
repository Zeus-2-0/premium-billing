package com.brihaspathee.zeus.service.impl;

import com.brihaspathee.zeus.domain.repository.AccountRepository;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.service.interfaces.AccountService;
import com.brihaspathee.zeus.service.interfaces.DataCleanUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:56 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.impl
 * To change this template use File | Settings | File and Code Template
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DataCleanUpServiceImpl implements DataCleanUpService {

    /**
     * Payload tracker helper instance
     */
    private final PayloadTrackerHelper payloadTrackerHelper;

    /**
     * Account Service instance
     */
    private final AccountService accountService;

    /**
     * Delete all transactional data
     */
    @Override
    public void deleteAll() {
        accountService.deleletAll();
        payloadTrackerHelper.deleteAll();
    }
}
