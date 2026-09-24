package com.devtrack.application.service;

import com.devtrack.application.port.in.CreateProjectUseCase;
import com.devtrack.application.port.out.ProjectRepositoryPort;
import com.devtrack.domain.Project;
import com.devtrack.domain.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProjectServiceTest {

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;

    @Test
    void createsProjectWithPlannedStatusAndPersistsItThroughThePort() {
        CreateProjectService service = new CreateProjectService(projectRepositoryPort);
        CreateProjectUseCase.Command command = new CreateProjectUseCase.Command("New Project", "Description");

        when(projectRepositoryPort.save(any(Project.class)))
                .thenAnswer(invocation -> {
                    Project toSave = invocation.getArgument(0);
                    return new Project(1L, toSave.getName(), toSave.getDescription(), toSave.getStatus());
                });

        Project result = service.create(command);

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepositoryPort).save(captor.capture());

        Project savedArgument = captor.getValue();
        assertThat(savedArgument.getName()).isEqualTo("New Project");
        assertThat(savedArgument.getStatus()).isEqualTo(ProjectStatus.PLANNED);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("New Project");
        assertThat(result.getDescription()).isEqualTo("Description");
        assertThat(result.getStatus()).isEqualTo(ProjectStatus.PLANNED);
    }
}
