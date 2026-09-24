package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.in.UpdateTaskUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UpdateTaskUseCase}.
 */
@Service
public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    /**
     * @param taskRepositoryPort the outbound port used to load and save the task
     */
    public UpdateTaskService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Task update(Long id, Command command) {
        Task task = taskRepositoryPort.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.rename(command.title());
        task.updateDescription(command.description());
        task.changeStatus(command.status());
        task.changePriority(command.priority());

        return taskRepositoryPort.save(task);
    }
}
