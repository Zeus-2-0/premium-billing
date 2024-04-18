package com.brihaspathee.zeus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 18, April 2024
 * Time: 11:08 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.config
 * To change this template use File | Settings | File and Code Template
 */
@Component
public class WebClientConfig {

    @Bean
    public WebClient getWebClient(){
        return WebClient.builder().build();
    }
}
