package com.devtrack.application.port.in;

import com.devtrack.domain.Task;
import com.devtrack.domain.TaskPriority;

/**
 * Inbound port for creating a new {@link Task} under an existing project.
 */
public interface CreateTaskUseCase {

    /**
     * Creates and persists a new task in {@code TODO} status.
     *
     * @param command the data needed to create the task
     * @return the created task, including its generated identifier
     * @throws com.devtrack.application.exception.ProjectNotFoundException if {@code command.projectId()}
     *                                                                     does not match an existing project
     */
    Task create(Command command);

    /**
     * Input data for {@link #create(Command)}.
     *
     * @param title       the task title; must not be blank
     * @param description a free-text description; may be {@code null}
     * @param priority    the task priority; must not be {@code null}
     * @param projectId   the identifier of the owning project; must not be {@code null}
     */
    record Command(String title, String description, TaskPriority priority, Long projectId) {
    }
}
