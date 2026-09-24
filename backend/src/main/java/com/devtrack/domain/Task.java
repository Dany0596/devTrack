package com.devtrack.domain;

import java.util.Objects;

/**
 * Pure domain entity representing a unit of work belonging to a {@link Project}.
 * <p>
 * The owning project is referenced by identifier ({@link #getProjectId()}) rather
 * than by embedding a {@link Project} instance, keeping the two aggregates
 * decoupled: a {@code Task} can be loaded, validated and persisted without ever
 * needing a full {@code Project} object in memory.
 */
public class Task {

    private final Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private final Long projectId;

    /**
     * Reconstructs a task in a known state, typically from persistence.
     *
     * @param id          the persistent identifier, or {@code null} for a task not yet saved
     * @param title       the task title; must not be blank
     * @param description a free-text description; may be {@code null}
     * @param status      the current lifecycle status; must not be {@code null}
     * @param priority    the task priority; must not be {@code null}
     * @param projectId   the identifier of the owning project; must not be {@code null}
     * @throws IllegalArgumentException if {@code title} is {@code null} or blank
     * @throws NullPointerException     if {@code status}, {@code priority} or {@code projectId} is {@code null}
     */
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority, Long projectId) {
        this.id = id;
        this.title = requireNonBlank(title);
        this.description = description;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.priority = Objects.requireNonNull(priority, "priority must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");
    }

    /**
     * Creates a brand-new task, not yet persisted, starting in {@link TaskStatus#TODO}.
     *
     * @param title       the task title; must not be blank
     * @param description a free-text description; may be {@code null}
     * @param priority    the task priority; must not be {@code null}
     * @param projectId   the identifier of the owning project; must not be {@code null}
     * @return a new, transient {@code Task} with no identifier yet
     */
    public static Task createNew(String title, String description, TaskPriority priority, Long projectId) {
        return new Task(null, title, description, TaskStatus.TODO, priority, projectId);
    }

    /**
     * Changes the task's title.
     *
     * @param newTitle the new title; must not be blank
     * @throws IllegalArgumentException if {@code newTitle} is {@code null} or blank
     */
    public void rename(String newTitle) {
        this.title = requireNonBlank(newTitle);
    }

    /**
     * Replaces the task's description.
     *
     * @param newDescription the new description; may be {@code null}
     */
    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Moves the task to a different lifecycle status.
     *
     * @param newStatus the new status; must not be {@code null}
     * @throws NullPointerException if {@code newStatus} is {@code null}
     */
    public void changeStatus(TaskStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "status must not be null");
    }

    /**
     * Changes the task's priority.
     *
     * @param newPriority the new priority; must not be {@code null}
     * @throws NullPointerException if {@code newPriority} is {@code null}
     */
    public void changePriority(TaskPriority newPriority) {
        this.priority = Objects.requireNonNull(newPriority, "priority must not be null");
    }

    private static String requireNonBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        return value;
    }

    /**
     * @return the persistent identifier, or {@code null} if the task has not been saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the task title; never blank
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the task description; may be {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the current lifecycle status; never {@code null}
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @return the current priority; never {@code null}
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * @return the identifier of the project this task belongs to; never {@code null}
     */
    public Long getProjectId() {
        return projectId;
    }
}
