package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.in.CreateTaskUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link CreateTaskUseCase}.
 * <p>
 * Validates that the target project actually exists before creating the task,
 * since a task cannot meaningfully exist without its owning project.
 */
@Service
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final ProjectRepositoryPort projectRepositoryPort;

    /**
     * @param taskRepositoryPort    the outbound port used to persist the new task
     * @param projectRepositoryPort the outbound port used to verify the owning project exists
     */
    public CreateTaskService(TaskRepositoryPort taskRepositoryPort, ProjectRepositoryPort projectRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.projectRepositoryPort = projectRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task create(Command command) {
        if (!projectRepositoryPort.existsById(command.projectId())) {
            throw new ProjectNotFoundException(command.projectId());
        }

        Task task = Task.createNew(command.title(), command.description(), command.priority(), command.projectId());
        return taskRepositoryPort.save(task);
    }
}
