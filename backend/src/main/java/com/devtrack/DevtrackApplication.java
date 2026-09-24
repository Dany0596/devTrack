package com.devtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the DevTrack backend.
 * <p>
 * Bootstraps the Spring Boot application context, which wires the hexagonal
 * architecture layers together: {@code domain}, {@code application} and
 * {@code infrastructure}.
 */
@SpringBootApplication
public class DevtrackApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments forwarded to Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(DevtrackApplication.class, args);
    }
}
