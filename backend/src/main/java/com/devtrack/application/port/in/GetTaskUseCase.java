package com.devtrack.application.port.in;

import com.devtrack.domain.Task;

/**
 * Inbound port for retrieving a single {@link Task} by its identifier.
 */
public interface GetTaskUseCase {

    /**
     * Looks up a task by identifier.
     *
     * @param id the task identifier; must not be {@code null}
     * @return the matching task
     * @throws com.devtrack.application.exception.TaskNotFoundException if no task exists with the given id
     */
    Task getById(Long id);
}
