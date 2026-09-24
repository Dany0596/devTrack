package com.devtrack.infrastructure.persistence;

import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import com.devtrack.infrastructure.persistence.mapper.TimeEntryPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Outbound adapter implementing {@link TimeEntryRepositoryPort} on top of
 * Spring Data JPA.
 * <p>
 * Translates every call to/from {@link TimeEntryJpaEntity} via
 * {@link com.devtrack.infrastructure.persistence.mapper.TimeEntryPersistenceMapper},
 * so the domain and application layers never see a JPA type.
 */
@Component
public class TimeEntryPersistenceAdapter implements TimeEntryRepositoryPort {

    private final TimeEntryJpaRepository timeEntryJpaRepository;

    /**
     * @param timeEntryJpaRepository the underlying Spring Data JPA repository
     */
    public TimeEntryPersistenceAdapter(TimeEntryJpaRepository timeEntryJpaRepository) {
        this.timeEntryJpaRepository = timeEntryJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeEntry save(TimeEntry timeEntry) {
        TimeEntryJpaEntity saved = timeEntryJpaRepository.save(TimeEntryPersistenceMapper.toEntity(timeEntry));
        return TimeEntryPersistenceMapper.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TimeEntry> findById(Long id) {
        return timeEntryJpaRepository.findById(id).map(TimeEntryPersistenceMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeEntry> findAll() {
        return timeEntryJpaRepository.findAll().stream()
                .map(TimeEntryPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeEntry> findByTaskId(Long taskId) {
        return timeEntryJpaRepository.findByTaskId(taskId).stream()
                .map(TimeEntryPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return timeEntryJpaRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        timeEntryJpaRepository.deleteById(id);
    }
}
