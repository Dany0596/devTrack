package com.devtrack.application.port.in;

import com.devtrack.domain.Task;
import com.devtrack.domain.TaskPriority;
import com.devtrack.domain.TaskStatus;

/**
 * Inbound port for updating an existing {@link Task}.
 * <p>
 * The owning project cannot be changed through this use case; a task is
 * always created under, and stays under, the same project.
 */
public interface UpdateTaskUseCase {

    /**
     * Replaces the title, description, status and priority of an existing task.
     *
     * @param id      the identifier of the task to update; must not be {@code null}
     * @param command the new values to apply
     * @return the updated task
     * @throws com.devtrack.application.exception.TaskNotFoundException if no task exists with the given id
     */
    Task update(Long id, Command command);

    /**
     * Input data for {@link #update(Long, Command)}.
     *
     * @param title       the new task title; must not be blank
     * @param description the new free-text description; may be {@code null}
     * @param status      the new lifecycle status; must not be {@code null}
     * @param priority    the new priority; must not be {@code null}
     */
    record Command(String title, String description, TaskStatus status, TaskPriority priority) {
    }
}
