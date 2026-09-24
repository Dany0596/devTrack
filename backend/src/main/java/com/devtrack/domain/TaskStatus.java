package com.devtrack.domain;

/**
 * Lifecycle states a {@link Task} can be in.
 */
public enum TaskStatus {

    /** The task has not been started yet. */
    TODO,

    /** The task is currently being worked on. */
    IN_PROGRESS,

    /** The task has been completed. */
    DONE
}
