package com.devtrack.application.service;

import com.devtrack.application.port.in.ListTasksUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link ListTasksUseCase}.
 */
@Service
public class ListTasksService implements ListTasksUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    /**
     * @param taskRepositoryPort the outbound port used to fetch tasks
     */
    public ListTasksService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> listAll() {
        return taskRepositoryPort.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> listByProjectId(Long projectId) {
        return taskRepositoryPort.findByProjectId(projectId);
    }
}
