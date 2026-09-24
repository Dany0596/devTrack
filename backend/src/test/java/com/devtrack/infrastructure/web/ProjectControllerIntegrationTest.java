package com.devtrack.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudLifecycleThroughController() throws Exception {
        String createPayload = objectMapper.writeValueAsString(
                new CreatePayload("Website Revamp", "Redesign the marketing site"));

        String createResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Website Revamp")))
                .andExpect(jsonPath("$.status", is("PLANNED")))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Website Revamp")));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d)]", id).exists());

        String updatePayload = objectMapper.writeValueAsString(
                new UpdatePayload("Website Revamp v2", "Updated scope", "ACTIVE"));

        mockMvc.perform(put("/api/projects/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Website Revamp v2")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));

        mockMvc.perform(delete("/api/projects/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsCreationWithBlankName() throws Exception {
        String invalidPayload = objectMapper.writeValueAsString(new CreatePayload("", "no name provided"));

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")));
    }

    private record CreatePayload(String name, String description) {
    }

    private record UpdatePayload(String name, String description, String status) {
    }
}
