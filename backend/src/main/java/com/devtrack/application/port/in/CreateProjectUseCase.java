package com.devtrack.application.port.in;

import com.devtrack.domain.Project;

/**
 * Inbound port for creating a new {@link Project}.
 * <p>
 * Implemented by an application service and driven by inbound adapters
 * (e.g. the REST controller).
 */
public interface CreateProjectUseCase {

    /**
     * Creates and persists a new project in {@code PLANNED} status.
     *
     * @param command the data needed to create the project
     * @return the created project, including its generated identifier
     */
    Project create(Command command);

    /**
     * Input data for {@link #create(Command)}.
     *
     * @param name        the project name; must not be blank
     * @param description a free-text description; may be {@code null}
     */
    record Command(String name, String description) {
    }
}
