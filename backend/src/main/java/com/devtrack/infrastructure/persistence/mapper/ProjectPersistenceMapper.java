package com.devtrack.infrastructure.persistence.mapper;

import com.devtrack.domain.Project;
import com.devtrack.infrastructure.persistence.ProjectJpaEntity;

/**
 * Stateless mapper between the domain model ({@link Project}) and the JPA
 * persistence model ({@link ProjectJpaEntity}).
 */
public final class ProjectPersistenceMapper {

    private ProjectPersistenceMapper() {
    }

    /**
     * Converts a domain project into its JPA entity representation.
     *
     * @param domain the domain project
     * @return the corresponding JPA entity
     */
    public static ProjectJpaEntity toEntity(Project domain) {
        return new ProjectJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getStatus()
        );
    }

    /**
     * Converts a JPA entity into its domain representation.
     *
     * @param entity the JPA entity
     * @return the corresponding domain project
     */
    public static Project toDomain(ProjectJpaEntity entity) {
        return new Project(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStatus()
        );
    }
}
