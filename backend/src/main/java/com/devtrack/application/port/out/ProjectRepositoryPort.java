package com.devtrack.application.port.out;

import com.devtrack.domain.Project;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port: the application layer depends on this abstraction only.
 * The persistence adapter (infrastructure.persistence) implements it.
 */
public interface ProjectRepositoryPort {

    /**
     * Persists a project, inserting it if it has no identifier yet or updating it otherwise.
     *
     * @param project the project to save; must not be {@code null}
     * @return the persisted project, with its identifier populated
     */
    Project save(Project project);

    /**
     * Looks up a project by identifier.
     *
     * @param id the project identifier; must not be {@code null}
     * @return the matching project, or {@link Optional#empty()} if none exists
     */
    Optional<Project> findById(Long id);

    /**
     * Returns every stored project.
     *
     * @return all projects; never {@code null}, possibly empty
     */
    List<Project> findAll();

    /**
     * Checks whether a project with the given identifier exists.
     *
     * @param id the project identifier; must not be {@code null}
     * @return {@code true} if a project with that id exists, {@code false} otherwise
     */
    boolean existsById(Long id);

    /**
     * Deletes the project with the given identifier, if present.
     *
     * @param id the project identifier; must not be {@code null}
     */
    void deleteById(Long id);
}
