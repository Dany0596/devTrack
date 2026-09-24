package com.devtrack.infrastructure.persistence;

import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import com.devtrack.infrastructure.persistence.mapper.TaskPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Outbound adapter implementing {@link TaskRepositoryPort} on top of
 * Spring Data JPA.
 * <p>
 * Translates every call to/from {@link TaskJpaEntity} via
 * {@link com.devtrack.infrastructure.persistence.mapper.TaskPersistenceMapper},
 * so the domain and application layers never see a JPA type.
 */
@Component
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskJpaRepository;

    /**
     * @param taskJpaRepository the underlying Spring Data JPA repository
     */
    public TaskPersistenceAdapter(TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task save(Task task) {
        TaskJpaEntity saved = taskJpaRepository.save(TaskPersistenceMapper.toEntity(task));
        return TaskPersistenceMapper.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id).map(TaskPersistenceMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAll() {
        return taskJpaRepository.findAll().stream()
                .map(TaskPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findByProjectId(Long projectId) {
        return taskJpaRepository.findByProjectId(projectId).stream()
                .map(TaskPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return taskJpaRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }
}
