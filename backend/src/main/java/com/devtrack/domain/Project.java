package com.devtrack.domain;

import java.util.Objects;

/**
 * Pure domain entity. No persistence or web framework annotations here on purpose:
 * this class must be usable and testable without Spring or JPA on the classpath.
 * <p>
 * A {@code Project} is the top-level unit of work tracking in DevTrack; state
 * changes are expressed as intention-revealing methods ({@link #rename},
 * {@link #updateDescription}, {@link #changeStatus}) rather than raw setters,
 * so invariants (e.g. a non-blank name) are enforced in a single place.
 */
public class Project {

    private final Long id;
    private String name;
    private String description;
    private ProjectStatus status;

    /**
     * Reconstructs a project in a known state, typically from persistence.
     *
     * @param id          the persistent identifier, or {@code null} for a project not yet saved
     * @param name        the project name; must not be blank
     * @param description a free-text description; may be {@code null}
     * @param status      the current lifecycle status; must not be {@code null}
     * @throws IllegalArgumentException if {@code name} is {@code null} or blank
     * @throws NullPointerException     if {@code status} is {@code null}
     */
    public Project(Long id, String name, String description, ProjectStatus status) {
        this.id = id;
        this.name = requireNonBlank(name);
        this.description = description;
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    /**
     * Creates a brand-new project, not yet persisted, starting in
     * {@link ProjectStatus#PLANNED}.
     *
     * @param name        the project name; must not be blank
     * @param description a free-text description; may be {@code null}
     * @return a new, transient {@code Project} with no identifier yet
     * @throws IllegalArgumentException if {@code name} is {@code null} or blank
     */
    public static Project createNew(String name, String description) {
        return new Project(null, name, description, ProjectStatus.PLANNED);
    }

    /**
     * Changes the project's name.
     *
     * @param newName the new name; must not be blank
     * @throws IllegalArgumentException if {@code newName} is {@code null} or blank
     */
    public void rename(String newName) {
        this.name = requireNonBlank(newName);
    }

    /**
     * Replaces the project's description.
     *
     * @param newDescription the new description; may be {@code null}
     */
    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Moves the project to a different lifecycle status.
     *
     * @param newStatus the new status; must not be {@code null}
     * @throws NullPointerException if {@code newStatus} is {@code null}
     */
    public void changeStatus(ProjectStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "status must not be null");
    }

    private static String requireNonBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return value;
    }

    /**
     * @return the persistent identifier, or {@code null} if the project has not been saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the project name; never blank
     */
    public String getName() {
        return name;
    }

    /**
     * @return the project description; may be {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the current lifecycle status; never {@code null}
     */
    public ProjectStatus getStatus() {
        return status;
    }
}
