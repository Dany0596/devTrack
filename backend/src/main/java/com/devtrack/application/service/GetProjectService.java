package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.in.GetProjectUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link GetProjectUseCase}.
 */
@Service
public class GetProjectService implements GetProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    /**
     * @param projectRepositoryPort the outbound port used to look up the project
     */
    public GetProjectService(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Project getById(Long id) {
        return projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }
}
