package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.in.UpdateProjectUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UpdateProjectUseCase}.
 */
@Service
public class UpdateProjectService implements UpdateProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    /**
     * @param projectRepositoryPort the outbound port used to load and save the project
     */
    public UpdateProjectService(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Project update(Long id, Command command) {
        Project project = projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.rename(command.name());
        project.updateDescription(command.description());
        project.changeStatus(command.status());

        return projectRepositoryPort.save(project);
    }
}
