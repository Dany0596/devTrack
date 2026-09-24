package com.devtrack.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating a new project via
 * {@link com.devtrack.infrastructure.web.ProjectController#create}.
 * <p>
 * No {@code status} field: new projects always start in
 * {@link com.devtrack.domain.ProjectStatus#PLANNED}.
 *
 * @param name        the project name; must not be blank
 * @param description a free-text description; may be {@code null}
 */
public record CreateProjectRequest(

        @NotBlank(message = "name must not be blank")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description
) {
}
