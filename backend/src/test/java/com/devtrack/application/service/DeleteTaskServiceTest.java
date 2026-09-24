package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Test
    void deletesAnExistingTask() {
        DeleteTaskService service = new DeleteTaskService(taskRepositoryPort);

        when(taskRepositoryPort.existsById(1L)).thenReturn(true);

        service.deleteById(1L);

        verify(taskRepositoryPort).deleteById(1L);
    }

    @Test
    void throwsWhenTaskDoesNotExistAndNeverCallsDelete() {
        DeleteTaskService service = new DeleteTaskService(taskRepositoryPort);

        when(taskRepositoryPort.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(999L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepositoryPort, never()).deleteById(999L);
    }
}
