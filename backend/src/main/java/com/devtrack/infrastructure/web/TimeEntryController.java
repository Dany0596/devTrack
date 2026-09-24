package com.devtrack.infrastructure.web;

import com.devtrack.application.port.in.CreateTimeEntryUseCase;
import com.devtrack.application.port.in.DeleteTimeEntryUseCase;
import com.devtrack.application.port.in.GetTimeEntryUseCase;
import com.devtrack.application.port.in.ListTimeEntriesUseCase;
import com.devtrack.application.port.in.UpdateTimeEntryUseCase;
import com.devtrack.domain.TimeEntry;
import com.devtrack.infrastructure.web.dto.CreateTimeEntryRequest;
import com.devtrack.infrastructure.web.dto.TimeEntryRequest;
import com.devtrack.infrastructure.web.dto.TimeEntryResponse;
import com.devtrack.infrastructure.web.mapper.TimeEntryWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Inbound REST adapter exposing CRUD operations on {@link TimeEntry} records.
 * <p>
 * Listing entries scoped to a single task is handled by
 * {@link TaskTimeEntriesController} instead, keeping the URL hierarchy
 * ({@code /api/tasks/{taskId}/time-entries}) separate from this controller's
 * flat {@code /api/time-entries} resource.
 */
@RestController
@RequestMapping("/api/time-entries")
public class TimeEntryController {

    private final CreateTimeEntryUseCase createTimeEntryUseCase;
    private final GetTimeEntryUseCase getTimeEntryUseCase;
    private final ListTimeEntriesUseCase listTimeEntriesUseCase;
    private final UpdateTimeEntryUseCase updateTimeEntryUseCase;
    private final DeleteTimeEntryUseCase deleteTimeEntryUseCase;

    /**
     * @param createTimeEntryUseCase use case handling time entry creation
     * @param getTimeEntryUseCase    use case handling single-entry lookup
     * @param listTimeEntriesUseCase use case handling listing of entries
     * @param updateTimeEntryUseCase use case handling entry updates
     * @param deleteTimeEntryUseCase use case handling entry deletion
     */
    public TimeEntryController(CreateTimeEntryUseCase createTimeEntryUseCase,
                                GetTimeEntryUseCase getTimeEntryUseCase,
                                ListTimeEntriesUseCase listTimeEntriesUseCase,
                                UpdateTimeEntryUseCase updateTimeEntryUseCase,
                                DeleteTimeEntryUseCase deleteTimeEntryUseCase) {
        this.createTimeEntryUseCase = createTimeEntryUseCase;
        this.getTimeEntryUseCase = getTimeEntryUseCase;
        this.listTimeEntriesUseCase = listTimeEntriesUseCase;
        this.updateTimeEntryUseCase = updateTimeEntryUseCase;
        this.deleteTimeEntryUseCase = deleteTimeEntryUseCase;
    }

    /**
     * Creates a new time entry under an existing task.
     *
     * @param request the entry data; validated via Bean Validation
     * @return {@code 201 Created} with the created entry
     */
    @PostMapping
    public ResponseEntity<TimeEntryResponse> create(@Valid @RequestBody CreateTimeEntryRequest request) {
        TimeEntry created = createTimeEntryUseCase.create(TimeEntryWebMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(TimeEntryWebMapper.toResponse(created));
    }

    /**
     * Retrieves a single time entry by identifier.
     *
     * @param id the time entry identifier
     * @return {@code 200 OK} with the entry, or {@code 404 Not Found} if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryResponse> getById(@PathVariable Long id) {
        TimeEntry timeEntry = getTimeEntryUseCase.getById(id);
        return ResponseEntity.ok(TimeEntryWebMapper.toResponse(timeEntry));
    }

    /**
     * Lists every time entry, across all tasks.
     *
     * @return {@code 200 OK} with all entries; the list is empty if none exist
     */
    @GetMapping
    public ResponseEntity<List<TimeEntryResponse>> listAll() {
        List<TimeEntryResponse> response = listTimeEntriesUseCase.listAll().stream()
                .map(TimeEntryWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing time entry's date, hours and notes.
     *
     * @param id      the identifier of the entry to update
     * @param request the new entry data; validated via Bean Validation
     * @return {@code 200 OK} with the updated entry, or {@code 404 Not Found} if it does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimeEntryResponse> update(@PathVariable Long id, @Valid @RequestBody TimeEntryRequest request) {
        TimeEntry updated = updateTimeEntryUseCase.update(id, TimeEntryWebMapper.toUpdateCommand(request));
        return ResponseEntity.ok(TimeEntryWebMapper.toResponse(updated));
    }

    /**
     * Deletes a time entry.
     *
     * @param id the identifier of the entry to delete
     * @return {@code 204 No Content} on success, or {@code 404 Not Found} if it does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteTimeEntryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
