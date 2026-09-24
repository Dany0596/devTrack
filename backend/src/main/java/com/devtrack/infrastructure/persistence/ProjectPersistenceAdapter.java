package com.devtrack.infrastructure.persistence;

import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import com.devtrack.infrastructure.persistence.mapper.ProjectPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Outbound adapter implementing {@link ProjectRepositoryPort} on top of
 * Spring Data JPA.
 * <p>
 * Translates every call to/from {@link ProjectJpaEntity} via
 * {@link com.devtrack.infrastructure.persistence.mapper.ProjectPersistenceMapper},
 * so the domain and application layers never see a JPA type.
 */
@Component
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private final ProjectJpaRepository projectJpaRepository;

    /**
     * @param projectJpaRepository the underlying Spring Data JPA repository
     */
    public ProjectPersistenceAdapter(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project save(Project project) {
        ProjectJpaEntity saved = projectJpaRepository.save(ProjectPersistenceMapper.toEntity(project));
        return ProjectPersistenceMapper.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Project> findById(Long id) {
        return projectJpaRepository.findById(id).map(ProjectPersistenceMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Project> findAll() {
        return projectJpaRepository.findAll().stream()
                .map(ProjectPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return projectJpaRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        projectJpaRepository.deleteById(id);
    }
}
