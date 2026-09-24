package com.devtrack.infrastructure.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration.
 * <p>
 * Enables CORS for the {@code /api/**} endpoints so the Angular dev server
 * (running on a different origin, e.g. {@code http://localhost:4200}) can
 * call this backend directly during local development.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Wildcard port (not a fixed "4200") because ng serve falls back to 4201, 4202, ...
        // when the default port is busy. Restrict this to the real frontend origin before
        // deploying anywhere beyond a local machine.
        registry.addMapping("/api/**")
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
