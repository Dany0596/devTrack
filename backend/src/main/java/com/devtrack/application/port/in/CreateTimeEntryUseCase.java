package com.devtrack.application.port.in;

import com.devtrack.domain.TimeEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inbound port for creating a new {@link TimeEntry} against an existing task.
 */
public interface CreateTimeEntryUseCase {

    /**
     * Creates and persists a new time entry.
     *
     * @param command the data needed to create the entry
     * @return the created entry, including its generated identifier
     * @throws com.devtrack.application.exception.TaskNotFoundException if {@code command.taskId()}
     *                                                                  does not match an existing task
     */
    TimeEntry create(Command command);

    /**
     * Input data for {@link #create(Command)}.
     *
     * @param taskId the identifier of the owning task; must not be {@code null}
     * @param date   the date the hours were worked; must not be {@code null}
     * @param hours  the number of hours worked; must not be {@code null} and must be positive
     * @param notes  a free-text note; may be {@code null}
     */
    record Command(Long taskId, LocalDate date, BigDecimal hours, String notes) {
    }
}
