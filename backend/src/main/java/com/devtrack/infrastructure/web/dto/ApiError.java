package com.devtrack.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

/**
 * Uniform error payload returned by {@link com.devtrack.infrastructure.web.GlobalExceptionHandler}
 * for every failed request.
 *
 * @param timestamp when the error was produced
 * @param status    the HTTP status code
 * @param error     the HTTP status reason phrase (e.g. {@code "Not Found"})
 * @param message   a human-readable summary of the failure
 * @param details   optional per-field validation messages; empty when not applicable
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<String> details
) {
    /**
     * Creates an error with no additional details.
     *
     * @param status  the HTTP status code
     * @param error   the HTTP status reason phrase
     * @param message a human-readable summary of the failure
     * @return a new {@code ApiError} timestamped with the current instant
     */
    public static ApiError of(int status, String error, String message) {
        return new ApiError(Instant.now(), status, error, message, List.of());
    }

    /**
     * Creates an error with additional details, typically per-field validation messages.
     *
     * @param status  the HTTP status code
     * @param error   the HTTP status reason phrase
     * @param message a human-readable summary of the failure
     * @param details per-field or per-issue messages
     * @return a new {@code ApiError} timestamped with the current instant
     */
    public static ApiError of(int status, String error, String message, List<String> details) {
        return new ApiError(Instant.now(), status, error, message, details);
    }
}
