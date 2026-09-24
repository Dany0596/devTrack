package com.devtrack.application.port.in;

import com.devtrack.domain.Project;

/**
 * Inbound port for retrieving a single {@link Project} by its identifier.
 */
public interface GetProjectUseCase {

    /**
     * Looks up a project by identifier.
     *
     * @param id the project identifier; must not be {@code null}
     * @return the matching project
     * @throws com.devtrack.application.exception.ProjectNotFoundException if no project exists with the given id
     */
    Project getById(Long id);
}
