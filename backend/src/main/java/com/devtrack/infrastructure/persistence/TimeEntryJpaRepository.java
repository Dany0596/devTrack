package com.devtrack.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link TimeEntryJpaEntity}.
 * <p>
 * Used only by {@link TimeEntryPersistenceAdapter}; the rest of the application
 * depends solely on the {@link com.devtrack.application.port.out.TimeEntryRepositoryPort}
 * abstraction.
 */
public interface TimeEntryJpaRepository extends JpaRepository<TimeEntryJpaEntity, Long> {

    /**
     * Finds every time entry logged against a given task.
     *
     * @param taskId the owning task's identifier
     * @return matching entities; never {@code null}, possibly empty
     */
    List<TimeEntryJpaEntity> findByTaskId(Long taskId);
}
