package com.devtrack.application.port.in;

/**
 * Inbound port for deleting a {@link com.devtrack.domain.Task}.
 */
public interface DeleteTaskUseCase {

    /**
     * Deletes the task with the given identifier.
     *
     * @param id the task identifier; must not be {@code null}
     * @throws com.devtrack.application.exception.TaskNotFoundException if no task exists with the given id
     */
    void deleteById(Long id);
}
