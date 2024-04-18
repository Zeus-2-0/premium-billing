package com.brihaspathee.zeus.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 18, April 2024
 * Time: 11:07 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.config
 * To change this template use File | Settings | File and Code Template
 */
@Configuration
@Profile("clean")
public class CleanDB {

    @Bean
    public FlywayMigrationStrategy clean(){
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
