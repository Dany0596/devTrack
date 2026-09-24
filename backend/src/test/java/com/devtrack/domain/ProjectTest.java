package com.devtrack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectTest {

    @Test
    void createNewStartsInPlannedStatusWithNoId() {
        Project project = Project.createNew("Website Revamp", "Redesign the marketing site");

        assertThat(project.getId()).isNull();
        assertThat(project.getName()).isEqualTo("Website Revamp");
        assertThat(project.getDescription()).isEqualTo("Redesign the marketing site");
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.PLANNED);
    }

    @Test
    void rejectsBlankNameOnConstruction() {
        assertThatThrownBy(() -> new Project(1L, "  ", "desc", ProjectStatus.PLANNED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }

    @Test
    void rejectsNullNameOnConstruction() {
        assertThatThrownBy(() -> new Project(1L, null, "desc", ProjectStatus.PLANNED))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNullStatusOnConstruction() {
        assertThatThrownBy(() -> new Project(1L, "Name", "desc", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void renameReplacesTheName() {
        Project project = Project.createNew("Old Name", null);

        project.rename("New Name");

        assertThat(project.getName()).isEqualTo("New Name");
    }

    @Test
    void renameRejectsBlankValue() {
        Project project = Project.createNew("Old Name", null);

        assertThatThrownBy(() -> project.rename(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateDescriptionAllowsNull() {
        Project project = Project.createNew("Name", "Initial description");

        project.updateDescription(null);

        assertThat(project.getDescription()).isNull();
    }

    @Test
    void changeStatusMovesToNewStatus() {
        Project project = Project.createNew("Name", null);

        project.changeStatus(ProjectStatus.ACTIVE);

        assertThat(project.getStatus()).isEqualTo(ProjectStatus.ACTIVE);
    }

    @Test
    void changeStatusRejectsNull() {
        Project project = Project.createNew("Name", null);

        assertThatThrownBy(() -> project.changeStatus(null))
                .isInstanceOf(NullPointerException.class);
    }
}
