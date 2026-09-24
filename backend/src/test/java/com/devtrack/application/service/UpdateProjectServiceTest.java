package com.devtrack.application.service;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.port.in.UpdateProjectUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import com.devtrack.domain.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProjectServiceTest {

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;

    @Test
    void updatesNameDescriptionAndStatusOfAnExistingProject() {
        UpdateProjectService service = new UpdateProjectService(projectRepositoryPort);
        Project existing = new Project(1L, "Old Name", "Old description", ProjectStatus.PLANNED);

        when(projectRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepositoryPort.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateProjectUseCase.Command command = new UpdateProjectUseCase.Command("New Name", "New description", ProjectStatus.ACTIVE);
        Project result = service.update(1L, command);

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepositoryPort).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("New Name");
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDescription()).isEqualTo("New description");
        assertThat(result.getStatus()).isEqualTo(ProjectStatus.ACTIVE);
    }

    @Test
    void throwsWhenProjectDoesNotExist() {
        UpdateProjectService service = new UpdateProjectService(projectRepositoryPort);
        UpdateProjectUseCase.Command command = new UpdateProjectUseCase.Command("Name", null, ProjectStatus.ACTIVE);

        when(projectRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(999L, command))
                .isInstanceOf(ProjectNotFoundException.class);
    }
}
