package com.devtrack.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TimeEntryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudLifecycleThroughControllerIncludingTaskScopedListing() throws Exception {
        Long projectId = createProject("Backend Revamp", "API rework");
        Long taskId = createTask("Design schema", projectId);

        String createPayload = objectMapper.writeValueAsString(
                new CreateTimeEntryPayload(taskId, "2026-09-24", "2.5", "Pairing session"));

        String createResponse = mockMvc.perform(post("/api/time-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId", is(taskId.intValue())))
                .andExpect(jsonPath("$.hours", is(2.5)))
                .andReturn().getResponse().getContentAsString();

        Long entryId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/time-entries/{id}", entryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes", is("Pairing session")));

        mockMvc.perform(get("/api/tasks/{taskId}/time-entries", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(entryId.intValue())));

        String updatePayload = objectMapper.writeValueAsString(
                new TimeEntryPayload("2026-09-25", "4.0", "Finished the diagram"));

        mockMvc.perform(put("/api/time-entries/{id}", entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is("2026-09-25")))
                .andExpect(jsonPath("$.hours", is(4.0)));

        mockMvc.perform(delete("/api/time-entries/{id}", entryId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/time-entries/{id}", entryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsTimeEntryCreationForUnknownTask() throws Exception {
        String payload = objectMapper.writeValueAsString(
                new CreateTimeEntryPayload(999999L, LocalDate.now().toString(), "1.0", null));

        mockMvc.perform(post("/api/time-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsNonPositiveHours() throws Exception {
        Long projectId = createProject("Rejects Test Project", null);
        Long taskId = createTask("Some task", projectId);

        String payload = objectMapper.writeValueAsString(
                new CreateTimeEntryPayload(taskId, LocalDate.now().toString(), "0", null));

        mockMvc.perform(post("/api/time-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    private Long createProject(String name, String description) throws Exception {
        String payload = objectMapper.writeValueAsString(new CreateProjectPayload(name, description));
        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long createTask(String title, Long projectId) throws Exception {
        String payload = objectMapper.writeValueAsString(new CreateTaskPayload(title, null, "MEDIUM", projectId));
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }

    private record CreateProjectPayload(String name, String description) {
    }

    private record CreateTaskPayload(String title, String description, String priority, Long projectId) {
    }

    private record CreateTimeEntryPayload(Long taskId, String date, String hours, String notes) {
    }

    private record TimeEntryPayload(String date, String hours, String notes) {
    }
}
