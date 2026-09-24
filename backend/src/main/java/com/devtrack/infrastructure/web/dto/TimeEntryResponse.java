package com.devtrack.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * API representation of a {@link com.devtrack.domain.TimeEntry} returned by
 * {@link com.devtrack.infrastructure.web.TimeEntryController}.
 *
 * @param id     the time entry identifier
 * @param taskId the identifier of the owning task
 * @param date   the date the hours were worked
 * @param hours  the number of hours worked
 * @param notes  a free-text note; may be {@code null}
 */
public record TimeEntryResponse(
        Long id,
        Long taskId,
        LocalDate date,
        BigDecimal hours,
        String notes
) {
}
