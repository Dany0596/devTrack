package com.devtrack.application.service;

import com.devtrack.application.exception.TimeEntryNotFoundException;
import com.devtrack.application.port.in.UpdateTimeEntryUseCase;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UpdateTimeEntryUseCase}.
 */
@Service
public class UpdateTimeEntryService implements UpdateTimeEntryUseCase {

    private final TimeEntryRepositoryPort timeEntryRepositoryPort;

    /**
     * @param timeEntryRepositoryPort the outbound port used to load and save the entry
     */
    public UpdateTimeEntryService(TimeEntryRepositoryPort timeEntryRepositoryPort) {
        this.timeEntryRepositoryPort = timeEntryRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TimeEntry update(Long id, Command command) {
        TimeEntry timeEntry = timeEntryRepositoryPort.findById(id)
                .orElseThrow(() -> new TimeEntryNotFoundException(id));

        timeEntry.changeDate(command.date());
        timeEntry.changeHours(command.hours());
        timeEntry.updateNotes(command.notes());

        return timeEntryRepositoryPort.save(timeEntry);
    }
}
