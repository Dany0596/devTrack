package com.devtrack.application.exception;

/**
 * Thrown when a {@link com.devtrack.domain.Project} is looked up by identifier
 * but no such project exists.
 * <p>
 * Translated to an HTTP 404 response by
 * {@link com.devtrack.infrastructure.web.GlobalExceptionHandler}.
 */
public class ProjectNotFoundException extends RuntimeException {

    /**
     * @param id the identifier that could not be resolved to a project
     */
    public ProjectNotFoundException(Long id) {
        super("Project not found with id=" + id);
    }
}
