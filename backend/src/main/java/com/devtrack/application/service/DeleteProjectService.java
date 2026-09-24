package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.in.DeleteProjectUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link DeleteProjectUseCase}.
 */
@Service
public class DeleteProjectService implements DeleteProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    /**
     * @param projectRepositoryPort the outbound port used to check existence and delete the project
     */
    public DeleteProjectService(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!projectRepositoryPort.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepositoryPort.deleteById(id);
    }
}
