package com.devtrack.application.port.out;

import com.devtrack.domain.Task;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for {@link Task} persistence. The application layer depends
 * on this abstraction only; the persistence adapter
 * (infrastructure.persistence) implements it.
 */
public interface TaskRepositoryPort {

    /**
     * Persists a task, inserting it if it has no identifier yet or updating it otherwise.
     *
     * @param task the task to save; must not be {@code null}
     * @return the persisted task, with its identifier populated
     */
    Task save(Task task);

    /**
     * Looks up a task by identifier.
     *
     * @param id the task identifier; must not be {@code null}
     * @return the matching task, or {@link Optional#empty()} if none exists
     */
    Optional<Task> findById(Long id);

    /**
     * Returns every stored task.
     *
     * @return all tasks; never {@code null}, possibly empty
     */
    List<Task> findAll();

    /**
     * Returns every task belonging to a given project.
     *
     * @param projectId the identifier of the owning project; must not be {@code null}
     * @return matching tasks; never {@code null}, possibly empty
     */
    List<Task> findByProjectId(Long projectId);

    /**
     * Checks whether a task with the given identifier exists.
     *
     * @param id the task identifier; must not be {@code null}
     * @return {@code true} if a task with that id exists, {@code false} otherwise
     */
    boolean existsById(Long id);

    /**
     * Deletes the task with the given identifier, if present.
     *
     * @param id the task identifier; must not be {@code null}
     */
    void deleteById(Long id);
}
