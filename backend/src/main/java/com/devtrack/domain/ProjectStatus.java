package com.devtrack.domain;

/**
 * Lifecycle states a {@link Project} can be in.
 */
public enum ProjectStatus {

    /** The project has been created but work has not started yet. */
    PLANNED,

    /** The project is currently being worked on. */
    ACTIVE,

    /** The project has been temporarily paused. */
    ON_HOLD,

    /** The project has been finished. */
    COMPLETED
}
