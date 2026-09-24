package com.devtrack.infrastructure.web;

import com.devtrack.application.port.in.CreateTaskUseCase;
import com.devtrack.application.port.in.DeleteTaskUseCase;
import com.devtrack.application.port.in.GetTaskUseCase;
import com.devtrack.application.port.in.ListTasksUseCase;
import com.devtrack.application.port.in.UpdateTaskUseCase;
import com.devtrack.domain.Task;
import com.devtrack.infrastructure.web.dto.CreateTaskRequest;
import com.devtrack.infrastructure.web.dto.TaskRequest;
import com.devtrack.infrastructure.web.dto.TaskResponse;
import com.devtrack.infrastructure.web.mapper.TaskWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Inbound REST adapter exposing CRUD operations on {@link Task}s.
 * <p>
 * Listing tasks scoped to a single project is handled by
 * {@link ProjectTasksController} instead, keeping the URL hierarchy
 * ({@code /api/projects/{projectId}/tasks}) separate from this controller's
 * flat {@code /api/tasks} resource.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    /**
     * @param createTaskUseCase use case handling task creation
     * @param getTaskUseCase    use case handling single-task lookup
     * @param listTasksUseCase  use case handling listing of tasks
     * @param updateTaskUseCase use case handling task updates
     * @param deleteTaskUseCase use case handling task deletion
     */
    public TaskController(CreateTaskUseCase createTaskUseCase,
                           GetTaskUseCase getTaskUseCase,
                           ListTasksUseCase listTasksUseCase,
                           UpdateTaskUseCase updateTaskUseCase,
                           DeleteTaskUseCase deleteTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.listTasksUseCase = listTasksUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    /**
     * Creates a new task under an existing project.
     *
     * @param request the task data; validated via Bean Validation
     * @return {@code 201 Created} with the created task
     */
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        Task created = createTaskUseCase.create(TaskWebMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskWebMapper.toResponse(created));
    }

    /**
     * Retrieves a single task by identifier.
     *
     * @param id the task identifier
     * @return {@code 200 OK} with the task, or {@code 404 Not Found} if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        Task task = getTaskUseCase.getById(id);
        return ResponseEntity.ok(TaskWebMapper.toResponse(task));
    }

    /**
     * Lists every task, across all projects.
     *
     * @return {@code 200 OK} with all tasks; the list is empty if none exist
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> listAll() {
        List<TaskResponse> response = listTasksUseCase.listAll().stream()
                .map(TaskWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing task's title, description, status and priority.
     *
     * @param id      the identifier of the task to update
     * @param request the new task data; validated via Bean Validation
     * @return {@code 200 OK} with the updated task, or {@code 404 Not Found} if it does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        Task updated = updateTaskUseCase.update(id, TaskWebMapper.toUpdateCommand(request));
        return ResponseEntity.ok(TaskWebMapper.toResponse(updated));
    }

    /**
     * Deletes a task.
     *
     * @param id the identifier of the task to delete
     * @return {@code 204 No Content} on success, or {@code 404 Not Found} if it does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteTaskUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
