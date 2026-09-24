package com.devtrack.application.port.in;

import com.devtrack.domain.TimeEntry;

/**
 * Inbound port for retrieving a single {@link TimeEntry} by its identifier.
 */
public interface GetTimeEntryUseCase {

    /**
     * Looks up a time entry by identifier.
     *
     * @param id the time entry identifier; must not be {@code null}
     * @return the matching time entry
     * @throws com.devtrack.application.exception.TimeEntryNotFoundException if no entry exists with the given id
     */
    TimeEntry getById(Long id);
}
