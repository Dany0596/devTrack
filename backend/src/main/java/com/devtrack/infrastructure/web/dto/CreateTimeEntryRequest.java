package com.devtrack.infrastructure.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request body for creating a new time entry via
 * {@link com.devtrack.infrastructure.web.TimeEntryController#create}.
 *
 * @param taskId the identifier of the owning task; must not be {@code null}
 * @param date   the date the hours were worked; must not be {@code null}
 * @param hours  the number of hours worked; must be strictly positive
 * @param notes  a free-text note; may be {@code null}
 */
public record CreateTimeEntryRequest(

        @NotNull(message = "taskId must not be null")
        Long taskId,

        @NotNull(message = "date must not be null")
        LocalDate date,

        @NotNull(message = "hours must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "hours must be greater than zero")
        BigDecimal hours,

        @Size(max = 2000, message = "notes must be at most 2000 characters")
        String notes
) {
}
