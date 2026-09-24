package com.devtrack.infrastructure.web;

import com.devtrack.application.port.in.ListTasksUseCase;
import com.devtrack.infrastructure.web.dto.TaskResponse;
import com.devtrack.infrastructure.web.mapper.TaskWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Inbound REST adapter exposing the tasks belonging to a single project,
 * under the nested {@code /api/projects/{projectId}/tasks} resource.
 * <p>
 * Kept separate from {@link TaskController} so each controller maps to a
 * single, coherent URL hierarchy.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class ProjectTasksController {

    private final ListTasksUseCase listTasksUseCase;

    /**
     * @param listTasksUseCase use case handling listing of tasks
     */
    public ProjectTasksController(ListTasksUseCase listTasksUseCase) {
        this.listTasksUseCase = listTasksUseCase;
    }

    /**
     * Lists every task belonging to a given project.
     *
     * @param projectId the identifier of the owning project
     * @return {@code 200 OK} with the project's tasks; the list is empty if none exist
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> listByProject(@PathVariable Long projectId) {
        List<TaskResponse> response = listTasksUseCase.listByProjectId(projectId).stream()
                .map(TaskWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
