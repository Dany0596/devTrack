package com.devtrack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskTest {

    @Test
    void createNewStartsInTodoStatusWithNoId() {
        Task task = Task.createNew("Design schema", "Draft the ER diagram", TaskPriority.HIGH, 1L);

        assertThat(task.getId()).isNull();
        assertThat(task.getTitle()).isEqualTo("Design schema");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(task.getProjectId()).isEqualTo(1L);
    }

    @Test
    void rejectsBlankTitleOnConstruction() {
        assertThatThrownBy(() -> new Task(1L, " ", null, TaskStatus.TODO, TaskPriority.LOW, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("title");
    }

    @Test
    void rejectsNullProjectIdOnConstruction() {
        assertThatThrownBy(() -> new Task(1L, "Title", null, TaskStatus.TODO, TaskPriority.LOW, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("projectId");
    }

    @Test
    void rejectsNullPriorityOnConstruction() {
        assertThatThrownBy(() -> new Task(1L, "Title", null, TaskStatus.TODO, null, 1L))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("priority");
    }

    @Test
    void renameReplacesTheTitle() {
        Task task = Task.createNew("Old title", null, TaskPriority.LOW, 1L);

        task.rename("New title");

        assertThat(task.getTitle()).isEqualTo("New title");
    }

    @Test
    void changeStatusMovesToNewStatus() {
        Task task = Task.createNew("Title", null, TaskPriority.LOW, 1L);

        task.changeStatus(TaskStatus.IN_PROGRESS);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void changePriorityMovesToNewPriority() {
        Task task = Task.createNew("Title", null, TaskPriority.LOW, 1L);

        task.changePriority(TaskPriority.URGENT);

        assertThat(task.getPriority()).isEqualTo(TaskPriority.URGENT);
    }

    @Test
    void changePriorityRejectsNull() {
        Task task = Task.createNew("Title", null, TaskPriority.LOW, 1L);

        assertThatThrownBy(() -> task.changePriority(null))
                .isInstanceOf(NullPointerException.class);
    }
}
