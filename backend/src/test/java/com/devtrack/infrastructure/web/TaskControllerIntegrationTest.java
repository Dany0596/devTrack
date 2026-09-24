package com.devtrack.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudLifecycleThroughControllerIncludingProjectScopedListing() throws Exception {
        String projectPayload = objectMapper.writeValueAsString(new CreateProjectPayload("Backend Revamp", "API rework"));
        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectPayload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long projectId = objectMapper.readTree(projectResponse).get("id").asLong();

        String createTaskPayload = objectMapper.writeValueAsString(
                new CreateTaskPayload("Design schema", "Draft the ER diagram", "HIGH", projectId));

        String createTaskResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTaskPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Design schema")))
                .andExpect(jsonPath("$.status", is("TODO")))
                .andExpect(jsonPath("$.projectId", is(projectId.intValue())))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(createTaskResponse).get("id").asLong();

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Design schema")));

        mockMvc.perform(get("/api/projects/{projectId}/tasks", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(taskId.intValue())));

        String updateTaskPayload = objectMapper.writeValueAsString(
                new TaskPayload("Design schema v2", "Refined ER diagram", "IN_PROGRESS", "URGENT"));

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTaskPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Design schema v2")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.priority", is("URGENT")));

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsTaskCreationForUnknownProject() throws Exception {
        String payload = objectMapper.writeValueAsString(
                new CreateTaskPayload("Orphan task", null, "LOW", 999999L));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());
    }

    private record CreateProjectPayload(String name, String description) {
    }

    private record CreateTaskPayload(String title, String description, String priority, Long projectId) {
    }

    private record TaskPayload(String title, String description, String status, String priority) {
    }
}
