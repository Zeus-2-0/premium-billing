package com.brihaspathee.zeus.broker.consumer;

import com.brihaspathee.zeus.broker.message.request.BillingUpdateRequest;
import com.brihaspathee.zeus.broker.message.response.BillingUpdateResponse;
import com.brihaspathee.zeus.broker.producer.BillingUpdateResponseProducer;
import com.brihaspathee.zeus.constants.ZeusServiceNames;
import com.brihaspathee.zeus.constants.ZeusTopics;
import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.message.Acknowledgement;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.brihaspathee.zeus.service.interfaces.BillingUpdateProcessor;
import com.brihaspathee.zeus.util.ZeusRandomStringGenerator;
import com.brihaspathee.zeus.validator.request.ProcessingValidationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:50 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.consumer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BillingUpdateListener {

    /**
     * Object mapper instance to convert the JSON to object
     */
    private final ObjectMapper objectMapper;

    /**
     * Payload tracker helper instance to create the payload tracker record
     */
    private final PayloadTrackerHelper payloadTrackerHelper;

    /**
     * Payload tracker detail helper instance to create the payload tracker detail record
     */
    private final PayloadTrackerDetailHelper payloadTrackerDetailHelper;

    /**
     * Billing Update response producer to send the billing update response back to APS
     */
    private final BillingUpdateResponseProducer billingUpdateResponseProducer;

    /**
     * The instance of billing update processor
     */
    private final BillingUpdateProcessor billingUpdateProcessor;

    /**
     * Listen to the topic to receive the request from APS for billing update
     * @param consumerRecord
     * @return
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = ZeusTopics.BILLING_UPDATE_REQ)
    @SendTo(ZeusTopics.BILLING_UPDATE_ACK)
    public ZeusMessagePayload<Acknowledgement> listen(
            ConsumerRecord<String, ZeusMessagePayload<BillingUpdateRequest>> consumerRecord)
    throws JsonProcessingException {
        Headers headers = consumerRecord.headers();
        log.info("Headers are:");
        headers.forEach(header -> {
            log.info("key: {}, value: {}", header.key(), new String(header.value()));
        });
        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(consumerRecord.value());
        // Convert it to Zeus Message Payload
        ZeusMessagePayload<BillingUpdateRequest> messagePayload = objectMapper.readValue(
                valueAsString,
                new TypeReference<ZeusMessagePayload<BillingUpdateRequest>>(){});
        log.info("APS Billing Update received for account:{}", messagePayload.getPayload().getAccountDto().getAccountNumber());

        // Save the payload to the payload tracker
        PayloadTracker payloadTracker = savePayloadTracker(valueAsString, messagePayload);

        // Create the acknowledgment back to transaction manager service

        ZeusMessagePayload<Acknowledgement> ack = createAcknowledgment(payloadTracker);

        // Perform validations
        performBillingUpdate(messagePayload, payloadTracker);

        log.info("After the call to perform the billing updates");

        return ack;
    }

    /**
     * Perform the updates requested for the member's billing account
     * @param messagePayload - The message payload that contains the billing update request
     * @param payloadTracker - The payload tracker created for the payload received
     */
    private void performBillingUpdate(ZeusMessagePayload<BillingUpdateRequest> messagePayload,
                                      PayloadTracker payloadTracker) throws JsonProcessingException {
//        try{
//            Thread.sleep(30000);
//        }catch (InterruptedException e){
//            log.error("The thread sleep was interrupted:{}", e.toString());
//        }
        log.info("The request payload id:{}", messagePayload.getPayloadId());
        BillingUpdateRequest billingUpdateRequest = messagePayload.getPayload();
        billingUpdateProcessor.processBillingUpdates(billingUpdateRequest)
                .subscribe(billingUpdateResponse -> {
            billingUpdateResponse.setAccountNumber(messagePayload.getPayload().getAccountDto().getAccountNumber());
            billingUpdateResponse.setZtcn(messagePayload.getPayload().getZrcn());
            billingUpdateResponse.setResponseId(ZeusRandomStringGenerator.randomString(15));
            billingUpdateResponse.setRequestPayloadId(messagePayload.getPayloadId());
            try {
                billingUpdateResponseProducer.sendBillingUpdateResponse(billingUpdateResponse, payloadTracker);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method will save the payload tracker
     * @param valueAsString
     * @param messagePayload
     * @return
     */
    private PayloadTracker savePayloadTracker(String valueAsString, ZeusMessagePayload<BillingUpdateRequest> messagePayload) {
        PayloadTracker payloadTracker = PayloadTracker.builder()
                .payloadId(messagePayload.getPayloadId())
                .payload_key(messagePayload.getPayload().getZrcn())
                .payload_key_type_code(messagePayload.getPayload().getZrcnTypeCode())
                .payloadDirectionTypeCode("INBOUND")
                .sourceDestinations(messagePayload.getMessageMetadata().getMessageSource())
                .payload(valueAsString)
                .build();
        payloadTracker = payloadTrackerHelper.createPayloadTracker(payloadTracker);
        return payloadTracker;
    }

    /**
     * Create the acknowledgement to send back to transaction manager service
     * @param payloadTracker
     * @return
     * @throws JsonProcessingException
     */
    private ZeusMessagePayload<Acknowledgement> createAcknowledgment(
            PayloadTracker payloadTracker) throws JsonProcessingException {
        String[] messageDestinations = {ZeusServiceNames.ACCOUNT_PROCESSOR_SERVICE};
        String ackId = ZeusRandomStringGenerator.randomString(15);
        ZeusMessagePayload<Acknowledgement> ack = ZeusMessagePayload.<Acknowledgement>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageDestination(messageDestinations)
                        .messageSource(ZeusServiceNames.PREMIUM_BILLING)
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(Acknowledgement.builder()
                        .ackId(ackId)
                        .requestPayloadId(payloadTracker.getPayloadId())
                        .build())
                .build();
        String ackAsString = objectMapper.writeValueAsString(ack);

        // Store the acknowledgement in the detail table
        PayloadTrackerDetail payloadTrackerDetail = PayloadTrackerDetail.builder()
                .payloadTracker(payloadTracker)
                .responseTypeCode("ACKNOWLEDGEMENT")
                .responsePayload(ackAsString)
                .responsePayloadId(ackId)
                .payloadDirectionTypeCode("OUTBOUND")
                .sourceDestinations(StringUtils.join(messageDestinations, ','))
                .build();
        payloadTrackerDetailHelper.createPayloadTrackerDetail(payloadTrackerDetail);
        return ack;
    }
}
