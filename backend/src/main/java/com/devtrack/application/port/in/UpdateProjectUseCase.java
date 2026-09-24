package com.devtrack.application.port.in;

import com.devtrack.domain.Project;
import com.devtrack.domain.ProjectStatus;

/**
 * Inbound port for updating an existing {@link Project}.
 */
public interface UpdateProjectUseCase {

    /**
     * Replaces the name, description and status of an existing project.
     *
     * @param id      the identifier of the project to update; must not be {@code null}
     * @param command the new values to apply
     * @return the updated project
     * @throws com.devtrack.application.exception.ProjectNotFoundException if no project exists with the given id
     */
    Project update(Long id, Command command);

    /**
     * Input data for {@link #update(Long, Command)}.
     *
     * @param name        the new project name; must not be blank
     * @param description the new free-text description; may be {@code null}
     * @param status      the new lifecycle status; must not be {@code null}
     */
    record Command(String name, String description, ProjectStatus status) {
    }
}
