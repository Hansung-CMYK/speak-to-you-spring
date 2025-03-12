package com.cmyk.ego.speaktoyouspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SpeakToYouSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpeakToYouSpringApplication.class, args);
    }

}
