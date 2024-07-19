package com.brihaspathee.zeus.service.impl;

import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.transaction.FileDetailDto;
import com.brihaspathee.zeus.service.interfaces.MemberManagementService;
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
 * Date: 17, July 2024
 * Time: 3:35 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagementServiceImpl implements MemberManagementService {

    /**
     * The reference data host
     */
    @Value("${url.host.member-mgmt}")
    private String memberMgmtHost;

    /**
     * Webclient to connect with other rest APIs
     */
    private final WebClient webClient;

    /**
     * Update member management service with the enrollment span updates
     * @param enrollmentSpanDto
     */
    @Override
    public void updateEnrollmentSpan(EnrollmentSpanDto enrollmentSpanDto) {
        log.info("Updating enrollment span:{}", enrollmentSpanDto);
        String uri = memberMgmtHost + "zeus/account/enrollmentSpan/update";
        Mono<ZeusApiResponse<String>> responseMono = webClient.post()
                .uri(uri)
                .body(Mono.just(enrollmentSpanDto), EnrollmentSpanDto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ZeusApiResponse<String>>() {});
        responseMono.doOnError(exception -> {
            log.info("Some Exception occurred:{}", exception.getMessage());
        }).subscribe(response -> log.info("Web Client API Response:{}", response.getResponse()));
    }
}
