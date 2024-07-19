package com.brihaspathee.zeus.service.interfaces;

import com.brihaspathee.zeus.broker.message.request.BillingUpdateRequest;
import com.brihaspathee.zeus.broker.message.response.BillingUpdateResponse;
import reactor.core.publisher.Mono;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 4:48 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface BillingUpdateProcessor {

    /**
     * Process the billing update request that was received
     * @param billingUpdateRequest
     */
    Mono<BillingUpdateResponse> processBillingUpdates(BillingUpdateRequest billingUpdateRequest);
}
