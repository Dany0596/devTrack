package com.devtrack.application.service;

import com.devtrack.application.port.in.CreateProjectUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link CreateProjectUseCase}.
 * <p>
 * Each use case gets its own dedicated service class rather than a shared
 * "god service", so responsibilities stay narrow and testable in isolation.
 */
@Service
public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    /**
     * @param projectRepositoryPort the outbound port used to persist the new project
     */
    public CreateProjectService(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project create(Command command) {
        Project project = Project.createNew(command.name(), command.description());
        return projectRepositoryPort.save(project);
    }
}
