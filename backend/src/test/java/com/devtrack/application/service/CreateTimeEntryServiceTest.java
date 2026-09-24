package com.devtrack.application.service;

import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.port.in.CreateTimeEntryUseCase;
import com.devtrack.application.port.out.TaskRepositoryPort;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTimeEntryServiceTest {

    @Mock
    private TimeEntryRepositoryPort timeEntryRepositoryPort;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Test
    void createsTimeEntryWhenTaskExists() {
        CreateTimeEntryService service = new CreateTimeEntryService(timeEntryRepositoryPort, taskRepositoryPort);
        CreateTimeEntryUseCase.Command command =
                new CreateTimeEntryUseCase.Command(1L, LocalDate.of(2026, 9, 24), new BigDecimal("2.5"), "Pairing session");

        when(taskRepositoryPort.existsById(1L)).thenReturn(true);
        when(timeEntryRepositoryPort.save(any(TimeEntry.class)))
                .thenAnswer(invocation -> {
                    TimeEntry toSave = invocation.getArgument(0);
                    return new TimeEntry(5L, toSave.getTaskId(), toSave.getDate(), toSave.getHours(), toSave.getNotes());
                });

        TimeEntry result = service.create(command);

        ArgumentCaptor<TimeEntry> captor = ArgumentCaptor.forClass(TimeEntry.class);
        verify(timeEntryRepositoryPort).save(captor.capture());

        TimeEntry savedArgument = captor.getValue();
        assertThat(savedArgument.getTaskId()).isEqualTo(1L);
        assertThat(savedArgument.getHours()).isEqualByComparingTo("2.5");

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getNotes()).isEqualTo("Pairing session");
    }

    @Test
    void rejectsCreationWhenTaskDoesNotExist() {
        CreateTimeEntryService service = new CreateTimeEntryService(timeEntryRepositoryPort, taskRepositoryPort);
        CreateTimeEntryUseCase.Command command =
                new CreateTimeEntryUseCase.Command(999L, LocalDate.now(), BigDecimal.ONE, null);

        when(taskRepositoryPort.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.create(command))
                .isInstanceOf(TaskNotFoundException.class);

        verify(timeEntryRepositoryPort, never()).save(any());
    }
}
