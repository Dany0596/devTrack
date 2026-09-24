package com.devtrack.infrastructure.persistence.mapper;

import com.devtrack.domain.Task;
import com.devtrack.infrastructure.persistence.TaskJpaEntity;

/**
 * Stateless mapper between the domain model ({@link Task}) and the JPA
 * persistence model ({@link TaskJpaEntity}).
 */
public final class TaskPersistenceMapper {

    private TaskPersistenceMapper() {
    }

    /**
     * Converts a domain task into its JPA entity representation.
     *
     * @param domain the domain task
     * @return the corresponding JPA entity
     */
    public static TaskJpaEntity toEntity(Task domain) {
        return new TaskJpaEntity(
                domain.getId(),
                domain.getTitle(),
                domain.getDescription(),
                domain.getStatus(),
                domain.getPriority(),
                domain.getProjectId()
        );
    }

    /**
     * Converts a JPA entity into its domain representation.
     *
     * @param entity the JPA entity
     * @return the corresponding domain task
     */
    public static Task toDomain(TaskJpaEntity entity) {
        return new Task(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getProjectId()
        );
    }
}
