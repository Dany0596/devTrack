package com.devtrack.infrastructure.web.mapper;

import com.devtrack.application.port.in.CreateTaskUseCase;
import com.devtrack.application.port.in.UpdateTaskUseCase;
import com.devtrack.domain.Task;
import com.devtrack.infrastructure.web.dto.CreateTaskRequest;
import com.devtrack.infrastructure.web.dto.TaskRequest;
import com.devtrack.infrastructure.web.dto.TaskResponse;

/**
 * Stateless mapper between web DTOs and application-layer types for {@link Task}.
 * <p>
 * Keeps {@link com.devtrack.infrastructure.web.TaskController} free of
 * translation logic and keeps the domain unaware of the web layer.
 */
public final class TaskWebMapper {

    private TaskWebMapper() {
    }

    /**
     * Converts an inbound create request into a {@link CreateTaskUseCase.Command}.
     *
     * @param request the validated request body
     * @return the corresponding use case command
     */
    public static CreateTaskUseCase.Command toCreateCommand(CreateTaskRequest request) {
        return new CreateTaskUseCase.Command(request.title(), request.description(), request.priority(), request.projectId());
    }

    /**
     * Converts an inbound update request into an {@link UpdateTaskUseCase.Command}.
     *
     * @param request the validated request body
     * @return the corresponding use case command
     */
    public static UpdateTaskUseCase.Command toUpdateCommand(TaskRequest request) {
        return new UpdateTaskUseCase.Command(request.title(), request.description(), request.status(), request.priority());
    }

    /**
     * Converts a domain task into its outbound API representation.
     *
     * @param task the domain task
     * @return the corresponding response DTO
     */
    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProjectId()
        );
    }
}
