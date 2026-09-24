package com.devtrack.application.service;

import com.devtrack.application.port.in.ListProjectsUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link ListProjectsUseCase}.
 */
@Service
public class ListProjectsService implements ListProjectsUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    /**
     * @param projectRepositoryPort the outbound port used to fetch all projects
     */
    public ListProjectsService(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Project> listAll() {
        return projectRepositoryPort.findAll();
    }
}
