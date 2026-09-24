package com.devtrack.infrastructure.web;

import com.devtrack.application.port.in.CreateProjectUseCase;
import com.devtrack.application.port.in.DeleteProjectUseCase;
import com.devtrack.application.port.in.GetProjectUseCase;
import com.devtrack.application.port.in.ListProjectsUseCase;
import com.devtrack.application.port.in.UpdateProjectUseCase;
import com.devtrack.domain.Project;
import com.devtrack.infrastructure.web.dto.CreateProjectRequest;
import com.devtrack.infrastructure.web.dto.ProjectRequest;
import com.devtrack.infrastructure.web.dto.ProjectResponse;
import com.devtrack.infrastructure.web.mapper.ProjectWebMapper;
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
 * Inbound REST adapter exposing CRUD operations on {@link Project}s.
 * <p>
 * Purely translates HTTP requests into use case calls and use case results
 * into HTTP responses; it holds no business logic itself. Validation errors
 * and domain exceptions are handled centrally by
 * {@link GlobalExceptionHandler}.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final GetProjectUseCase getProjectUseCase;
    private final ListProjectsUseCase listProjectsUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final DeleteProjectUseCase deleteProjectUseCase;

    /**
     * @param createProjectUseCase use case handling project creation
     * @param getProjectUseCase    use case handling single-project lookup
     * @param listProjectsUseCase  use case handling listing of all projects
     * @param updateProjectUseCase use case handling project updates
     * @param deleteProjectUseCase use case handling project deletion
     */
    public ProjectController(CreateProjectUseCase createProjectUseCase,
                              GetProjectUseCase getProjectUseCase,
                              ListProjectsUseCase listProjectsUseCase,
                              UpdateProjectUseCase updateProjectUseCase,
                              DeleteProjectUseCase deleteProjectUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.getProjectUseCase = getProjectUseCase;
        this.listProjectsUseCase = listProjectsUseCase;
        this.updateProjectUseCase = updateProjectUseCase;
        this.deleteProjectUseCase = deleteProjectUseCase;
    }

    /**
     * Creates a new project.
     *
     * @param request the project data; validated via Bean Validation
     * @return {@code 201 Created} with the created project
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody CreateProjectRequest request) {
        Project created = createProjectUseCase.create(ProjectWebMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectWebMapper.toResponse(created));
    }

    /**
     * Retrieves a single project by identifier.
     *
     * @param id the project identifier
     * @return {@code 200 OK} with the project, or {@code 404 Not Found} if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        Project project = getProjectUseCase.getById(id);
        return ResponseEntity.ok(ProjectWebMapper.toResponse(project));
    }

    /**
     * Lists every project.
     *
     * @return {@code 200 OK} with all projects; the list is empty if none exist
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listAll() {
        List<ProjectResponse> response = listProjectsUseCase.listAll().stream()
                .map(ProjectWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing project's name, description and status.
     *
     * @param id      the identifier of the project to update
     * @param request the new project data; validated via Bean Validation
     * @return {@code 200 OK} with the updated project, or {@code 404 Not Found} if it does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        Project updated = updateProjectUseCase.update(id, ProjectWebMapper.toUpdateCommand(request));
        return ResponseEntity.ok(ProjectWebMapper.toResponse(updated));
    }

    /**
     * Deletes a project.
     *
     * @param id the identifier of the project to delete
     * @return {@code 204 No Content} on success, or {@code 404 Not Found} if it does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProjectUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
