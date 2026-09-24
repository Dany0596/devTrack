package com.devtrack.infrastructure.web.mapper;

import com.devtrack.application.port.in.CreateTimeEntryUseCase;
import com.devtrack.application.port.in.UpdateTimeEntryUseCase;
import com.devtrack.domain.TimeEntry;
import com.devtrack.infrastructure.web.dto.CreateTimeEntryRequest;
import com.devtrack.infrastructure.web.dto.TimeEntryRequest;
import com.devtrack.infrastructure.web.dto.TimeEntryResponse;

/**
 * Stateless mapper between web DTOs and application-layer types for {@link TimeEntry}.
 * <p>
 * Keeps {@link com.devtrack.infrastructure.web.TimeEntryController} free of
 * translation logic and keeps the domain unaware of the web layer.
 */
public final class TimeEntryWebMapper {

    private TimeEntryWebMapper() {
    }

    /**
     * Converts an inbound create request into a {@link CreateTimeEntryUseCase.Command}.
     *
     * @param request the validated request body
     * @return the corresponding use case command
     */
    public static CreateTimeEntryUseCase.Command toCreateCommand(CreateTimeEntryRequest request) {
        return new CreateTimeEntryUseCase.Command(request.taskId(), request.date(), request.hours(), request.notes());
    }

    /**
     * Converts an inbound update request into an {@link UpdateTimeEntryUseCase.Command}.
     *
     * @param request the validated request body
     * @return the corresponding use case command
     */
    public static UpdateTimeEntryUseCase.Command toUpdateCommand(TimeEntryRequest request) {
        return new UpdateTimeEntryUseCase.Command(request.date(), request.hours(), request.notes());
    }

    /**
     * Converts a domain time entry into its outbound API representation.
     *
     * @param timeEntry the domain time entry
     * @return the corresponding response DTO
     */
    public static TimeEntryResponse toResponse(TimeEntry timeEntry) {
        return new TimeEntryResponse(
                timeEntry.getId(),
                timeEntry.getTaskId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                timeEntry.getNotes()
        );
    }
}
