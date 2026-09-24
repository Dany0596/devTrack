package com.devtrack.infrastructure.web.dto;

import com.devtrack.domain.ProjectStatus;

/**
 * API representation of a {@link com.devtrack.domain.Project} returned by
 * {@link com.devtrack.infrastructure.web.ProjectController}.
 *
 * @param id          the project identifier
 * @param name        the project name
 * @param description the project description; may be {@code null}
 * @param status      the current lifecycle status
 */
public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status
) {
}
