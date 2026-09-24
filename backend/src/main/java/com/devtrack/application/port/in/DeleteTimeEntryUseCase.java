package com.devtrack.application.port.in;

/**
 * Inbound port for deleting a {@link com.devtrack.domain.TimeEntry}.
 */
public interface DeleteTimeEntryUseCase {

    /**
     * Deletes the time entry with the given identifier.
     *
     * @param id the time entry identifier; must not be {@code null}
     * @throws com.devtrack.application.exception.TimeEntryNotFoundException if no entry exists with the given id
     */
    void deleteById(Long id);
}
