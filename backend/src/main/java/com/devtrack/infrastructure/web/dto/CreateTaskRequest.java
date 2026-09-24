package com.devtrack.infrastructure.web.dto;

import com.devtrack.domain.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating a new task via
 * {@link com.devtrack.infrastructure.web.TaskController#create}.
 * <p>
 * No {@code status} field: new tasks always start in
 * {@link com.devtrack.domain.TaskStatus#TODO}.
 *
 * @param title       the task title; must not be blank
 * @param description a free-text description; may be {@code null}
 * @param priority    the task priority; must not be {@code null}
 * @param projectId   the identifier of the owning project; must not be {@code null}
 */
public record CreateTaskRequest(

        @NotBlank(message = "title must not be blank")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "priority must not be null")
        TaskPriority priority,

        @NotNull(message = "projectId must not be null")
        Long projectId
) {
}
