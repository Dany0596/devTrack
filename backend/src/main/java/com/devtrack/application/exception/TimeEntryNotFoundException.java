package com.devtrack.application.exception;

/**
 * Thrown when a {@link com.devtrack.domain.TimeEntry} is looked up by identifier
 * but no such entry exists.
 * <p>
 * Translated to an HTTP 404 response by
 * {@link com.devtrack.infrastructure.web.GlobalExceptionHandler}.
 */
public class TimeEntryNotFoundException extends RuntimeException {

    /**
     * @param id the identifier that could not be resolved to a time entry
     */
    public TimeEntryNotFoundException(Long id) {
        super("Time entry not found with id=" + id);
    }
}
