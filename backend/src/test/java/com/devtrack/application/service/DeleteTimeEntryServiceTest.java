package com.devtrack.application.service;

import com.devtrack.application.exception.TimeEntryNotFoundException;
import com.devtrack.application.port.out.TimeEntryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTimeEntryServiceTest {

    @Mock
    private TimeEntryRepositoryPort timeEntryRepositoryPort;

    @Test
    void deletesAnExistingEntry() {
        DeleteTimeEntryService service = new DeleteTimeEntryService(timeEntryRepositoryPort);

        when(timeEntryRepositoryPort.existsById(1L)).thenReturn(true);

        service.deleteById(1L);

        verify(timeEntryRepositoryPort).deleteById(1L);
    }

    @Test
    void throwsWhenEntryDoesNotExistAndNeverCallsDelete() {
        DeleteTimeEntryService service = new DeleteTimeEntryService(timeEntryRepositoryPort);

        when(timeEntryRepositoryPort.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(999L))
                .isInstanceOf(TimeEntryNotFoundException.class);

        verify(timeEntryRepositoryPort, never()).deleteById(999L);
    }
}
