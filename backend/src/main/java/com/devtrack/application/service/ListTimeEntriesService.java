package com.devtrack.application.service;

import com.devtrack.application.port.in.ListTimeEntriesUseCase;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link ListTimeEntriesUseCase}.
 */
@Service
public class ListTimeEntriesService implements ListTimeEntriesUseCase {

    private final TimeEntryRepositoryPort timeEntryRepositoryPort;

    /**
     * @param timeEntryRepositoryPort the outbound port used to fetch entries
     */
    public ListTimeEntriesService(TimeEntryRepositoryPort timeEntryRepositoryPort) {
        this.timeEntryRepositoryPort = timeEntryRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TimeEntry> listAll() {
        return timeEntryRepositoryPort.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TimeEntry> listByTaskId(Long taskId) {
        return timeEntryRepositoryPort.findByTaskId(taskId);
    }
}
