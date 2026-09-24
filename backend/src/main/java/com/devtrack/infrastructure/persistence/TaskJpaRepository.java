package com.devtrack.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link TaskJpaEntity}.
 * <p>
 * Used only by {@link TaskPersistenceAdapter}; the rest of the application
 * depends solely on the {@link com.devtrack.application.port.out.TaskRepositoryPort}
 * abstraction.
 */
public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long> {

    /**
     * Finds every task belonging to a given project.
     *
     * @param projectId the owning project's identifier
     * @return matching entities; never {@code null}, possibly empty
     */
    List<TaskJpaEntity> findByProjectId(Long projectId);
}
