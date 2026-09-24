package com.devtrack.application.port.in;

import com.devtrack.domain.TimeEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inbound port for updating an existing {@link TimeEntry}.
 * <p>
 * The owning task cannot be changed through this use case; an entry is
 * always created under, and stays under, the same task.
 */
public interface UpdateTimeEntryUseCase {

    /**
     * Replaces the date, hours and notes of an existing time entry.
     *
     * @param id      the identifier of the entry to update; must not be {@code null}
     * @param command the new values to apply
     * @return the updated entry
     * @throws com.devtrack.application.exception.TimeEntryNotFoundException if no entry exists with the given id
     */
    TimeEntry update(Long id, Command command);

    /**
     * Input data for {@link #update(Long, Command)}.
     *
     * @param date  the new date; must not be {@code null}
     * @param hours the new hours value; must not be {@code null} and must be positive
     * @param notes the new free-text note; may be {@code null}
     */
    record Command(LocalDate date, BigDecimal hours, String notes) {
    }
}
