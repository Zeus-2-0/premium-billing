package com.brihaspathee.zeus.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 18, April 2024
 * Time: 11:08 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.config
 * To change this template use File | Settings | File and Code Template
 */
@OpenAPIDefinition
@Configuration
@Profile({"local", "clean"})
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${zeus-account-processor.api.version}") String apiVersion){
        List<Server> servers = new ArrayList<>();
        servers.add(new Server()
                .url("http://localhost:9004")
                .description("Development Server"));
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Zeus Premium Billing API")
                        .version(apiVersion)
                        .contact(new Contact()
                                .name("Balaji Varadharajan")
                                .url("http://www.zeus.com")
                                .email("vbalaji215@outlook.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/License-2.0.html"))
                )
                .servers(servers);
    }
}
