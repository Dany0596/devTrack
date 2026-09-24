package com.devtrack.application.port.in;

/**
 * Inbound port for deleting a {@link com.devtrack.domain.Project}.
 */
public interface DeleteProjectUseCase {

    /**
     * Deletes the project with the given identifier.
     *
     * @param id the project identifier; must not be {@code null}
     * @throws com.devtrack.application.exception.ProjectNotFoundException if no project exists with the given id
     */
    void deleteById(Long id);
}
