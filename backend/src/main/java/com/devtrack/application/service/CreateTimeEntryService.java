package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.in.CreateTimeEntryUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link CreateTimeEntryUseCase}.
 * <p>
 * Validates that the target task actually exists before creating the entry,
 * since a time entry cannot meaningfully exist without its owning task.
 */
@Service
public class CreateTimeEntryService implements CreateTimeEntryUseCase {

    private final TimeEntryRepositoryPort timeEntryRepositoryPort;
    private final TaskRepositoryPort taskRepositoryPort;

    /**
     * @param timeEntryRepositoryPort the outbound port used to persist the new entry
     * @param taskRepositoryPort      the outbound port used to verify the owning task exists
     */
    public CreateTimeEntryService(TimeEntryRepositoryPort timeEntryRepositoryPort, TaskRepositoryPort taskRepositoryPort) {
        this.timeEntryRepositoryPort = timeEntryRepositoryPort;
        this.taskRepositoryPort = taskRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeEntry create(Command command) {
        if (!taskRepositoryPort.existsById(command.taskId())) {
            throw new TaskNotFoundException(command.taskId());
        }

        TimeEntry timeEntry = TimeEntry.createNew(command.taskId(), command.date(), command.hours(), command.notes());
        return timeEntryRepositoryPort.save(timeEntry);
    }
}
