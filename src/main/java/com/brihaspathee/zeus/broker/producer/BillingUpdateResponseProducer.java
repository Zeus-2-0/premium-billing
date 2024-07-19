package com.brihaspathee.zeus.broker.producer;

import com.brihaspathee.zeus.broker.message.response.BillingUpdateResponse;
import com.brihaspathee.zeus.constants.ZeusServiceNames;
import com.brihaspathee.zeus.constants.ZeusTopics;
import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerDetailHelper;
import com.brihaspathee.zeus.helper.interfaces.PayloadTrackerHelper;
import com.brihaspathee.zeus.message.MessageMetadata;
import com.brihaspathee.zeus.message.ZeusMessagePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 10:09 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.broker.producer
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BillingUpdateResponseProducer {

    /**
     * Kafka template to produce and send messages
     */
    private final KafkaTemplate<String, ZeusMessagePayload<BillingUpdateResponse>> kafkaTemplate;

    /**
     * Object mapper that can covert the object into a string
     */
    private final ObjectMapper objectMapper;

    /**
     * Payload tracker detail helper instance to create the payload tracker detail record
     */
    private final PayloadTrackerDetailHelper payloadTrackerDetailHelper;

    /**
     * ListenableFutureCallback class that is used after success or failure of publishing the message
     */
    private final BillingUpdateResponseCallback billingUpdateResponseCallback;

    public void sendBillingUpdateResponse(BillingUpdateResponse billingUpdateResponse,
                                          PayloadTracker payloadTracker) throws JsonProcessingException {
        log.info("Inside the billing update response producer for account:{}", billingUpdateResponse.getAccountNumber());

        // Create the result payload that is to be sent to the member management service
        String[] messageDestinations = {ZeusServiceNames.ACCOUNT_PROCESSOR_SERVICE};
        ZeusMessagePayload<BillingUpdateResponse> messagePayload = ZeusMessagePayload.<BillingUpdateResponse>builder()
                .messageMetadata(MessageMetadata.builder()
                        .messageSource(ZeusServiceNames.PREMIUM_BILLING)
                        .messageDestination(messageDestinations)
                        .messageCreationTimestamp(LocalDateTime.now())
                        .build())
                .payload(billingUpdateResponse)
                .build();
        // Create the payload tracker detail record for the validation result payload
        PayloadTrackerDetail payloadTrackerDetail = createPayloadTrackerDetail(
                messagePayload, payloadTracker);
        billingUpdateResponseCallback.setBillingUpdateResponse(billingUpdateResponse);
        // Build the producer record
        ProducerRecord<String, ZeusMessagePayload<BillingUpdateResponse>> producerRecord =
                buildProducerRecord("Test Payload id", messagePayload);
        // Send to kafka topic
        kafkaTemplate.send(producerRecord);//.addCallback(transactionValidationResultCallback);
        log.info("After the sending the billing update response to account processing service is called");
    }

    /**
     * Create the payload tracker detail record
     * @param messagePayload
     * @return
     * @throws JsonProcessingException
     */
    private PayloadTrackerDetail createPayloadTrackerDetail(
            ZeusMessagePayload<BillingUpdateResponse> messagePayload,
            PayloadTracker payloadTracker) throws JsonProcessingException {
        // Convert the payload as String
        String valueAsString = objectMapper.writeValueAsString(messagePayload);
        // Store the response in the detail table
        PayloadTrackerDetail payloadTrackerDetail = PayloadTrackerDetail.builder()
                .payloadTracker(payloadTracker)
                .responseTypeCode("BILLING-UPDATE-RESPONSE")
                .responsePayload(valueAsString)
                .responsePayloadId(messagePayload.getPayload().getResponseId())
                .payloadDirectionTypeCode("OUTBOUND")
                .sourceDestinations(StringUtils.join(
                        messagePayload.getMessageMetadata().getMessageDestination(),
                        ','))
                .build();
        payloadTrackerDetailHelper.createPayloadTrackerDetail(payloadTrackerDetail);
        return payloadTrackerDetail;
    }

    /**
     * The method to build the producer record
     * @param payloadId
     * @param messagePayload
     */
    private ProducerRecord<String, ZeusMessagePayload<BillingUpdateResponse>> buildProducerRecord(
            String payloadId,
            ZeusMessagePayload<BillingUpdateResponse> messagePayload){
        RecordHeader messageHeader = new RecordHeader("Processing payload response id",
                "Processing payload response id".getBytes());
        return new ProducerRecord<>(ZeusTopics.BILLING_UPDATE_RESP,
                null,
                payloadId,
                messagePayload,
                Arrays.asList(messageHeader));
    }
}
