package com.devtrack.infrastructure.web.dto;

import com.devtrack.domain.TaskPriority;
import com.devtrack.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for updating an existing task via
 * {@link com.devtrack.infrastructure.web.TaskController#update}.
 * <p>
 * Unlike {@link CreateTaskRequest}, this includes {@code status} since an
 * update can transition the task's lifecycle state. The owning project
 * cannot be changed through this request.
 *
 * @param title       the new task title; must not be blank
 * @param description the new description; may be {@code null}
 * @param status      the new lifecycle status; must not be {@code null}
 * @param priority    the new priority; must not be {@code null}
 */
public record TaskRequest(

        @NotBlank(message = "title must not be blank")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "status must not be null")
        TaskStatus status,

        @NotNull(message = "priority must not be null")
        TaskPriority priority
) {
}
