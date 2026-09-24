package com.devtrack.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link ProjectJpaEntity}.
 * <p>
 * Used only by {@link ProjectPersistenceAdapter}; the rest of the application
 * depends solely on the {@link com.devtrack.application.port.out.ProjectRepositoryPort}
 * abstraction.
 */
public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, Long> {
}
