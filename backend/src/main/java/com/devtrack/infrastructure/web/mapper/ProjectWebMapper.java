package com.devtrack.infrastructure.web.mapper;

import com.devtrack.application.port.in.CreateProjectUseCase;
import com.devtrack.application.port.in.UpdateProjectUseCase;
import com.devtrack.domain.Project;
import com.devtrack.infrastructure.web.dto.CreateProjectRequest;
import com.devtrack.infrastructure.web.dto.ProjectRequest;
import com.devtrack.infrastructure.web.dto.ProjectResponse;

/**
 * Stateless mapper between web DTOs and application-layer types.
 * <p>
 * Keeps {@link com.devtrack.infrastructure.web.ProjectController} free of
 * translation logic and keeps the domain unaware of the web layer.
 */
public final class ProjectWebMapper {

    private ProjectWebMapper() {
    }

    /**
     * Converts an inbound create request into a {@link CreateProjectUseCase.Command}.
     *
     * @param request the validated request body
     * @return the corresponding use case command
     */
    public static CreateProjectUseCase.Command toCreateCommand(CreateProjectRequest request) {
        return new CreateProjectUseCase.Command(request.name(), request.description());
    }

    /**
     * Converts an inbound update request into an {@link UpdateProjectUseCase.Command}.
     *
     * @param request the validated request body
     * @return the corresponding use case command
     */
    public static UpdateProjectUseCase.Command toUpdateCommand(ProjectRequest request) {
        return new UpdateProjectUseCase.Command(request.name(), request.description(), request.status());
    }

    /**
     * Converts a domain project into its outbound API representation.
     *
     * @param project the domain project
     * @return the corresponding response DTO
     */
    public static ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus()
        );
    }
}
