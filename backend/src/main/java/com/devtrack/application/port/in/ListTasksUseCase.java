package com.devtrack.application.port.in;

import com.devtrack.domain.Task;

import java.util.List;

/**
 * Inbound port for listing {@link Task}s, either all of them or scoped to a project.
 */
public interface ListTasksUseCase {

    /**
     * Returns every task currently stored.
     *
     * @return all tasks, in no particular guaranteed order; never {@code null}, possibly empty
     */
    List<Task> listAll();

    /**
     * Returns every task belonging to a given project.
     *
     * @param projectId the identifier of the owning project; must not be {@code null}
     * @return all tasks for that project, in no particular guaranteed order; never {@code null}, possibly empty
     */
    List<Task> listByProjectId(Long projectId);
}
