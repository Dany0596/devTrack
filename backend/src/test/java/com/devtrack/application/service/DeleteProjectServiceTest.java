package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProjectServiceTest {

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;

    @Test
    void deletesAnExistingProject() {
        DeleteProjectService service = new DeleteProjectService(projectRepositoryPort);

        when(projectRepositoryPort.existsById(1L)).thenReturn(true);

        service.deleteById(1L);

        verify(projectRepositoryPort).deleteById(1L);
    }

    @Test
    void throwsWhenProjectDoesNotExistAndNeverCallsDelete() {
        DeleteProjectService service = new DeleteProjectService(projectRepositoryPort);

        when(projectRepositoryPort.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(999L))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepositoryPort, never()).deleteById(999L);
    }
}
