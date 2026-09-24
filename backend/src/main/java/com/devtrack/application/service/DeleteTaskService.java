package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.in.DeleteTaskUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link DeleteTaskUseCase}.
 */
@Service
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    /**
     * @param taskRepositoryPort the outbound port used to check existence and delete the task
     */
    public DeleteTaskService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!taskRepositoryPort.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepositoryPort.deleteById(id);
    }
}
