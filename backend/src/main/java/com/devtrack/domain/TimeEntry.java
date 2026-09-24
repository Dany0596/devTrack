package com.devtrack.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Pure domain entity representing hours worked on a {@link Task} on a given day.
 * <p>
 * The owning task is referenced by identifier ({@link #getTaskId()}) rather
 * than by embedding a {@link Task} instance, keeping the two aggregates
 * decoupled, consistent with how {@link Task} references its owning
 * {@link Project}.
 */
public class TimeEntry {

    private final Long id;
    private final Long taskId;
    private LocalDate date;
    private BigDecimal hours;
    private String notes;

    /**
     * Reconstructs a time entry in a known state, typically from persistence.
     *
     * @param id     the persistent identifier, or {@code null} for an entry not yet saved
     * @param taskId the identifier of the task this entry is logged against; must not be {@code null}
     * @param date   the date the hours were worked; must not be {@code null}
     * @param hours  the number of hours worked; must not be {@code null} and must be positive
     * @param notes  a free-text note; may be {@code null}
     * @throws NullPointerException     if {@code taskId}, {@code date} or {@code hours} is {@code null}
     * @throws IllegalArgumentException if {@code hours} is not positive
     */
    public TimeEntry(Long id, Long taskId, LocalDate date, BigDecimal hours, String notes) {
        this.id = id;
        this.taskId = Objects.requireNonNull(taskId, "taskId must not be null");
        this.date = Objects.requireNonNull(date, "date must not be null");
        this.hours = requirePositive(hours);
        this.notes = notes;
    }

    /**
     * Creates a brand-new time entry, not yet persisted.
     *
     * @param taskId the identifier of the task this entry is logged against; must not be {@code null}
     * @param date   the date the hours were worked; must not be {@code null}
     * @param hours  the number of hours worked; must not be {@code null} and must be positive
     * @param notes  a free-text note; may be {@code null}
     * @return a new, transient {@code TimeEntry} with no identifier yet
     */
    public static TimeEntry createNew(Long taskId, LocalDate date, BigDecimal hours, String notes) {
        return new TimeEntry(null, taskId, date, hours, notes);
    }

    /**
     * Changes the date the hours were worked.
     *
     * @param newDate the new date; must not be {@code null}
     * @throws NullPointerException if {@code newDate} is {@code null}
     */
    public void changeDate(LocalDate newDate) {
        this.date = Objects.requireNonNull(newDate, "date must not be null");
    }

    /**
     * Changes the number of hours worked.
     *
     * @param newHours the new hours value; must not be {@code null} and must be positive
     * @throws IllegalArgumentException if {@code newHours} is not positive
     */
    public void changeHours(BigDecimal newHours) {
        this.hours = requirePositive(newHours);
    }

    /**
     * Replaces the entry's notes.
     *
     * @param newNotes the new notes; may be {@code null}
     */
    public void updateNotes(String newNotes) {
        this.notes = newNotes;
    }

    private static BigDecimal requirePositive(BigDecimal value) {
        Objects.requireNonNull(value, "hours must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("hours must be positive");
        }
        return value;
    }

    /**
     * @return the persistent identifier, or {@code null} if the entry has not been saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the identifier of the task this entry is logged against; never {@code null}
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * @return the date the hours were worked; never {@code null}
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the number of hours worked; never {@code null}, always positive
     */
    public BigDecimal getHours() {
        return hours;
    }

    /**
     * @return a free-text note; may be {@code null}
     */
    public String getNotes() {
        return notes;
    }
}
