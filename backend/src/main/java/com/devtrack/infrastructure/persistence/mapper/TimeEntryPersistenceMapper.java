package com.devtrack.infrastructure.persistence.mapper;

import com.devtrack.domain.TimeEntry;
import com.devtrack.infrastructure.persistence.TimeEntryJpaEntity;

/**
 * Stateless mapper between the domain model ({@link TimeEntry}) and the JPA
 * persistence model ({@link TimeEntryJpaEntity}).
 */
public final class TimeEntryPersistenceMapper {

    private TimeEntryPersistenceMapper() {
    }

    /**
     * Converts a domain time entry into its JPA entity representation.
     *
     * @param domain the domain time entry
     * @return the corresponding JPA entity
     */
    public static TimeEntryJpaEntity toEntity(TimeEntry domain) {
        return new TimeEntryJpaEntity(
                domain.getId(),
                domain.getTaskId(),
                domain.getDate(),
                domain.getHours(),
                domain.getNotes()
        );
    }

    /**
     * Converts a JPA entity into its domain representation.
     *
     * @param entity the JPA entity
     * @return the corresponding domain time entry
     */
    public static TimeEntry toDomain(TimeEntryJpaEntity entity) {
        return new TimeEntry(
                entity.getId(),
                entity.getTaskId(),
                entity.getDate(),
                entity.getHours(),
                entity.getNotes()
        );
    }
}
