package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.in.CreateTaskUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.domain.Task;
import com.devtrack.domain.TaskPriority;
import com.devtrack.domain.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;

    @Test
    void createsTaskWithTodoStatusWhenProjectExists() {
        CreateTaskService service = new CreateTaskService(taskRepositoryPort, projectRepositoryPort);
        CreateTaskUseCase.Command command = new CreateTaskUseCase.Command("Design schema", "Draft the ER diagram", TaskPriority.HIGH, 1L);

        when(projectRepositoryPort.existsById(1L)).thenReturn(true);
        when(taskRepositoryPort.save(any(Task.class)))
                .thenAnswer(invocation -> {
                    Task toSave = invocation.getArgument(0);
                    return new Task(10L, toSave.getTitle(), toSave.getDescription(), toSave.getStatus(), toSave.getPriority(), toSave.getProjectId());
                });

        Task result = service.create(command);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepositoryPort).save(captor.capture());

        Task savedArgument = captor.getValue();
        assertThat(savedArgument.getTitle()).isEqualTo("Design schema");
        assertThat(savedArgument.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(savedArgument.getProjectId()).isEqualTo(1L);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getPriority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void rejectsCreationWhenProjectDoesNotExist() {
        CreateTaskService service = new CreateTaskService(taskRepositoryPort, projectRepositoryPort);
        CreateTaskUseCase.Command command = new CreateTaskUseCase.Command("Design schema", null, TaskPriority.LOW, 999L);

        when(projectRepositoryPort.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.create(command))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(taskRepositoryPort, never()).save(any());
    }
}
