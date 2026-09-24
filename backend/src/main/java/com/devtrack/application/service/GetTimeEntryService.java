package com.devtrack.application.service;

import com.devtrack.application.exception.TimeEntryNotFoundException;
import com.devtrack.application.port.in.GetTimeEntryUseCase;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link GetTimeEntryUseCase}.
 */
@Service
public class GetTimeEntryService implements GetTimeEntryUseCase {

    private final TimeEntryRepositoryPort timeEntryRepositoryPort;

    /**
     * @param timeEntryRepositoryPort the outbound port used to look up the entry
     */
    public GetTimeEntryService(TimeEntryRepositoryPort timeEntryRepositoryPort) {
        this.timeEntryRepositoryPort = timeEntryRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TimeEntry getById(Long id) {
        return timeEntryRepositoryPort.findById(id)
                .orElseThrow(() -> new TimeEntryNotFoundException(id));
    }
}
