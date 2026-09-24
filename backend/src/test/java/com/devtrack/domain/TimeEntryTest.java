package com.devtrack.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeEntryTest {

    @Test
    void createNewBuildsAnEntryWithNoId() {
        TimeEntry entry = TimeEntry.createNew(1L, LocalDate.of(2026, 9, 24), new BigDecimal("2.5"), "Pairing session");

        assertThat(entry.getId()).isNull();
        assertThat(entry.getTaskId()).isEqualTo(1L);
        assertThat(entry.getDate()).isEqualTo(LocalDate.of(2026, 9, 24));
        assertThat(entry.getHours()).isEqualByComparingTo("2.5");
        assertThat(entry.getNotes()).isEqualTo("Pairing session");
    }

    @Test
    void rejectsNullTaskIdOnConstruction() {
        assertThatThrownBy(() -> new TimeEntry(1L, null, LocalDate.now(), BigDecimal.ONE, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("taskId");
    }

    @Test
    void rejectsNullDateOnConstruction() {
        assertThatThrownBy(() -> new TimeEntry(1L, 1L, null, BigDecimal.ONE, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("date");
    }

    @Test
    void rejectsZeroHoursOnConstruction() {
        assertThatThrownBy(() -> new TimeEntry(1L, 1L, LocalDate.now(), BigDecimal.ZERO, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void rejectsNegativeHoursOnConstruction() {
        assertThatThrownBy(() -> new TimeEntry(1L, 1L, LocalDate.now(), new BigDecimal("-1"), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeDateReplacesTheDate() {
        TimeEntry entry = TimeEntry.createNew(1L, LocalDate.of(2026, 1, 1), BigDecimal.ONE, null);

        entry.changeDate(LocalDate.of(2026, 2, 2));

        assertThat(entry.getDate()).isEqualTo(LocalDate.of(2026, 2, 2));
    }

    @Test
    void changeHoursRejectsNonPositiveValue() {
        TimeEntry entry = TimeEntry.createNew(1L, LocalDate.now(), BigDecimal.ONE, null);

        assertThatThrownBy(() -> entry.changeHours(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateNotesAllowsNull() {
        TimeEntry entry = TimeEntry.createNew(1L, LocalDate.now(), BigDecimal.ONE, "Initial note");

        entry.updateNotes(null);

        assertThat(entry.getNotes()).isNull();
    }
}
