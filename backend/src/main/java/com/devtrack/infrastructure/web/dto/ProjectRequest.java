package com.devtrack.infrastructure.web.dto;

import com.devtrack.domain.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for updating an existing project via
 * {@link com.devtrack.infrastructure.web.ProjectController#update}.
 * <p>
 * Unlike {@link CreateProjectRequest}, this includes {@code status} since an
 * update can transition the project's lifecycle state.
 *
 * @param name        the new project name; must not be blank
 * @param description the new description; may be {@code null}
 * @param status      the new lifecycle status; must not be {@code null}
 */
public record ProjectRequest(

        @NotBlank(message = "name must not be blank")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "status must not be null")
        ProjectStatus status
) {
}
