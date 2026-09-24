package com.devtrack.infrastructure.web;

import com.devtrack.application.port.in.ListTimeEntriesUseCase;
import com.devtrack.infrastructure.web.dto.TimeEntryResponse;
import com.devtrack.infrastructure.web.mapper.TimeEntryWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Inbound REST adapter exposing the time entries logged against a single
 * task, under the nested {@code /api/tasks/{taskId}/time-entries} resource.
 * <p>
 * Kept separate from {@link TimeEntryController} so each controller maps to
 * a single, coherent URL hierarchy.
 */
@RestController
@RequestMapping("/api/tasks/{taskId}/time-entries")
public class TaskTimeEntriesController {

    private final ListTimeEntriesUseCase listTimeEntriesUseCase;

    /**
     * @param listTimeEntriesUseCase use case handling listing of time entries
     */
    public TaskTimeEntriesController(ListTimeEntriesUseCase listTimeEntriesUseCase) {
        this.listTimeEntriesUseCase = listTimeEntriesUseCase;
    }

    /**
     * Lists every time entry logged against a given task.
     *
     * @param taskId the identifier of the owning task
     * @return {@code 200 OK} with the task's time entries; the list is empty if none exist
     */
    @GetMapping
    public ResponseEntity<List<TimeEntryResponse>> listByTask(@PathVariable Long taskId) {
        List<TimeEntryResponse> response = listTimeEntriesUseCase.listByTaskId(taskId).stream()
                .map(TimeEntryWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
