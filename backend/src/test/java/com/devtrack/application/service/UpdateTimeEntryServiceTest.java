package com.devtrack.application.service;

import com.devtrack.application.exception.TimeEntryNotFoundException;
import com.devtrack.application.port.in.UpdateTimeEntryUseCase;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import com.devtrack.domain.TimeEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTimeEntryServiceTest {

    @Mock
    private TimeEntryRepositoryPort timeEntryRepositoryPort;

    @Test
    void updatesDateHoursAndNotesOfAnExistingEntry() {
        UpdateTimeEntryService service = new UpdateTimeEntryService(timeEntryRepositoryPort);
        TimeEntry existing = new TimeEntry(1L, 7L, LocalDate.of(2026, 1, 1), BigDecimal.ONE, "Old note");

        when(timeEntryRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(timeEntryRepositoryPort.save(any(TimeEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTimeEntryUseCase.Command command =
                new UpdateTimeEntryUseCase.Command(LocalDate.of(2026, 2, 2), new BigDecimal("4.0"), "New note");
        TimeEntry result = service.update(1L, command);

        assertThat(result.getDate()).isEqualTo(LocalDate.of(2026, 2, 2));
        assertThat(result.getHours()).isEqualByComparingTo("4.0");
        assertThat(result.getNotes()).isEqualTo("New note");
        assertThat(result.getTaskId()).isEqualTo(7L);
    }

    @Test
    void throwsWhenEntryDoesNotExist() {
        UpdateTimeEntryService service = new UpdateTimeEntryService(timeEntryRepositoryPort);
        UpdateTimeEntryUseCase.Command command = new UpdateTimeEntryUseCase.Command(LocalDate.now(), BigDecimal.ONE, null);

        when(timeEntryRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(999L, command))
                .isInstanceOf(TimeEntryNotFoundException.class);
    }
}
