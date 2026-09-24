package com.devtrack.infrastructure.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request body for updating an existing time entry via
 * {@link com.devtrack.infrastructure.web.TimeEntryController#update}.
 * <p>
 * The owning task cannot be changed through this request.
 *
 * @param date  the new date; must not be {@code null}
 * @param hours the new hours value; must be strictly positive
 * @param notes the new free-text note; may be {@code null}
 */
public record TimeEntryRequest(

        @NotNull(message = "date must not be null")
        LocalDate date,

        @NotNull(message = "hours must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "hours must be greater than zero")
        BigDecimal hours,

        @Size(max = 2000, message = "notes must be at most 2000 characters")
        String notes
) {
}
