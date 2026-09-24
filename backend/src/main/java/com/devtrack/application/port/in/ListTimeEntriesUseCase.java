package com.devtrack.application.port.in;

import com.devtrack.domain.TimeEntry;

import java.util.List;

/**
 * Inbound port for listing {@link TimeEntry} records, either all of them or scoped to a task.
 */
public interface ListTimeEntriesUseCase {

    /**
     * Returns every time entry currently stored.
     *
     * @return all entries, in no particular guaranteed order; never {@code null}, possibly empty
     */
    List<TimeEntry> listAll();

    /**
     * Returns every time entry logged against a given task.
     *
     * @param taskId the identifier of the owning task; must not be {@code null}
     * @return all entries for that task, in no particular guaranteed order; never {@code null}, possibly empty
     */
    List<TimeEntry> listByTaskId(Long taskId);
}
