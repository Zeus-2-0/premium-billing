package com.brihaspathee.zeus.web.resource.impl;

import com.brihaspathee.zeus.web.model.WelcomeDto;
import com.brihaspathee.zeus.web.resource.interfaces.WelcomeAPI;
import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 18, April 2024
 * Time: 11:13 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.web.resource.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class WelcomeAPIImpl implements WelcomeAPI {

    /**
     * Returns a simple welcome message for the service
     * @return
     */
    @Override
    public ResponseEntity<ZeusApiResponse<WelcomeDto>> getWelcomeMessage() {
        return ResponseEntity.ok(ZeusApiResponse.<WelcomeDto>builder()
                .developerMessage("Connection is good")
                .message("Hello, Welcome to Zeus Premium Billing Service")
                .statusCode(200)
                .status(HttpStatus.OK)
                //.timestamp(LocalDateTime.now())
                .response(WelcomeDto.builder()
                        .welcomeMessage("Hello, I am glad that you are here!!! I am sure you will have a great time")
                        .build())
                .build());
    }
}
