package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.in.GetTaskUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link GetTaskUseCase}.
 */
@Service
public class GetTaskService implements GetTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    /**
     * @param taskRepositoryPort the outbound port used to look up the task
     */
    public GetTaskService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepositoryPort.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
