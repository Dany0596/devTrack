package com.devtrack.application.service;

import com.devtrack.application.exception.TimeEntryNotFoundException;
import com.devtrack.application.port.in.DeleteTimeEntryUseCase;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link DeleteTimeEntryUseCase}.
 */
@Service
public class DeleteTimeEntryService implements DeleteTimeEntryUseCase {

    private final TimeEntryRepositoryPort timeEntryRepositoryPort;

    /**
     * @param timeEntryRepositoryPort the outbound port used to check existence and delete the entry
     */
    public DeleteTimeEntryService(TimeEntryRepositoryPort timeEntryRepositoryPort) {
        this.timeEntryRepositoryPort = timeEntryRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!timeEntryRepositoryPort.existsById(id)) {
            throw new TimeEntryNotFoundException(id);
        }
        timeEntryRepositoryPort.deleteById(id);
    }
}
