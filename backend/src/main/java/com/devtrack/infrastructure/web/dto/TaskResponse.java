package com.devtrack.infrastructure.web.dto;

import com.devtrack.domain.TaskPriority;
import com.devtrack.domain.TaskStatus;

/**
 * API representation of a {@link com.devtrack.domain.Task} returned by
 * {@link com.devtrack.infrastructure.web.TaskController}.
 *
 * @param id          the task identifier
 * @param title       the task title
 * @param description the task description; may be {@code null}
 * @param status      the current lifecycle status
 * @param priority    the current priority
 * @param projectId   the identifier of the owning project
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long projectId
) {
}
