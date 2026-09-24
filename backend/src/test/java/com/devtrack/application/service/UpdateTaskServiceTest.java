package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.in.UpdateTaskUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import com.devtrack.domain.TaskPriority;
import com.devtrack.domain.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Test
    void updatesTitleStatusAndPriorityOfAnExistingTask() {
        UpdateTaskService service = new UpdateTaskService(taskRepositoryPort);
        Task existing = new Task(1L, "Old title", null, TaskStatus.TODO, TaskPriority.LOW, 5L);

        when(taskRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskUseCase.Command command =
                new UpdateTaskUseCase.Command("New title", "New description", TaskStatus.DONE, TaskPriority.URGENT);
        Task result = service.update(1L, command);

        assertThat(result.getTitle()).isEqualTo("New title");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.DONE);
        assertThat(result.getPriority()).isEqualTo(TaskPriority.URGENT);
        assertThat(result.getProjectId()).isEqualTo(5L);
    }

    @Test
    void throwsWhenTaskDoesNotExist() {
        UpdateTaskService service = new UpdateTaskService(taskRepositoryPort);
        UpdateTaskUseCase.Command command = new UpdateTaskUseCase.Command("Title", null, TaskStatus.TODO, TaskPriority.LOW);

        when(taskRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(999L, command))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
