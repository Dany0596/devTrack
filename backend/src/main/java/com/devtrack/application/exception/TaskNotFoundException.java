package com.devtrack.application.exception;

/**
 * Thrown when a {@link com.devtrack.domain.Task} is looked up by identifier
 * but no such task exists.
 * <p>
 * Translated to an HTTP 404 response by
 * {@link com.devtrack.infrastructure.web.GlobalExceptionHandler}.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * @param id the identifier that could not be resolved to a task
     */
    public TaskNotFoundException(Long id) {
        super("Task not found with id=" + id);
    }
}
