package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.broker.message.response.BillingUpdateResponse;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import org.springframework.kafka.support.SendResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 10:10 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.producer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class BillingUpdateResponseCallback implements
        ListenableFutureCallback<SendResult<String, ZeusMessagePayload<BillingUpdateResponse>>> {

    /**
     * The message payload sent to the topic
     */
    private BillingUpdateResponse billingUpdateResponse;

    /**
     * Executed when there is a failure to publish the message
     * @param ex
     */
    @Override
    public void onFailure(Throwable ex) {
        log.info("The message to APS from Premium Billing failed to publis");
    }

    /**
     * Executed when the message was published successfully
     * @param result
     */
    @Override
    public void onSuccess(SendResult<String, ZeusMessagePayload<BillingUpdateResponse>> result) {
        log.info("The message to APS published successfully");
    }
}
