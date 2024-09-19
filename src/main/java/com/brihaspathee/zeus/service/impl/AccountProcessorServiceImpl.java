package com.brihaspathee.zeus.service.impl;

import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanStatusDto;
import com.brihaspathee.zeus.service.interfaces.AccountProcessorService;
import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 25, July 2024
 * Time: 6:11 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountProcessorServiceImpl implements AccountProcessorService {

    /**
     * The reference data host
     */
    @Value("${url.host.account-processor}")
    private String apsHost;

    /**
     * Webclient to connect with other rest APIs
     */
    private final WebClient webClient;

    /**
     * Get the status of the enrollment span
     * @param enrollmentSpanStatusDto - The object that contains the enrollment span for which the status is returned
     *                                and any prior enrollment spans
     *                                that are present for the account
     * @return - returns the status of the enrollment span
     */
    @Override
    public String getEnrollmentSpanStatus(EnrollmentSpanStatusDto enrollmentSpanStatusDto) {
        log.info("Enrollment Span Status DTO:{}", enrollmentSpanStatusDto);
        String uri = apsHost + "zeus/account-processor/span-status";
        ZeusApiResponse<String> response = webClient.post()
                .uri(uri)
                .body(Mono.just(enrollmentSpanStatusDto), EnrollmentSpanStatusDto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ZeusApiResponse<String>>() {}).block();
        assert response != null;
        return response.getResponse();

    }

}
