package com.devtrack.application.port.out;

import com.devtrack.domain.TimeEntry;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for {@link TimeEntry} persistence. The application layer
 * depends on this abstraction only; the persistence adapter
 * (infrastructure.persistence) implements it.
 */
public interface TimeEntryRepositoryPort {

    /**
     * Persists a time entry, inserting it if it has no identifier yet or updating it otherwise.
     *
     * @param timeEntry the entry to save; must not be {@code null}
     * @return the persisted entry, with its identifier populated
     */
    TimeEntry save(TimeEntry timeEntry);

    /**
     * Looks up a time entry by identifier.
     *
     * @param id the time entry identifier; must not be {@code null}
     * @return the matching entry, or {@link Optional#empty()} if none exists
     */
    Optional<TimeEntry> findById(Long id);

    /**
     * Returns every stored time entry.
     *
     * @return all entries; never {@code null}, possibly empty
     */
    List<TimeEntry> findAll();

    /**
     * Returns every time entry logged against a given task.
     *
     * @param taskId the identifier of the owning task; must not be {@code null}
     * @return matching entries; never {@code null}, possibly empty
     */
    List<TimeEntry> findByTaskId(Long taskId);

    /**
     * Checks whether a time entry with the given identifier exists.
     *
     * @param id the time entry identifier; must not be {@code null}
     * @return {@code true} if an entry with that id exists, {@code false} otherwise
     */
    boolean existsById(Long id);

    /**
     * Deletes the time entry with the given identifier, if present.
     *
     * @param id the time entry identifier; must not be {@code null}
     */
    void deleteById(Long id);
}
