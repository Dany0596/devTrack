package com.devtrack.application.port.in;

import com.devtrack.domain.Project;

import java.util.List;

/**
 * Inbound port for listing all known {@link Project}s.
 */
public interface ListProjectsUseCase {

    /**
     * Returns every project currently stored.
     *
     * @return all projects, in no particular guaranteed order; never {@code null}, possibly empty
     */
    List<Project> listAll();
}
