import { Component, OnInit, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ProjectCardComponent } from '../project-card/project-card.component';
import { ProjectFormComponent } from '../project-form/project-form.component';
import { ProjectService } from '../../project.service';
import { CreateProjectRequest, Project } from '../../project.model';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [ProjectCardComponent, ProjectFormComponent],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss'
})
export class ProjectListComponent implements OnInit {
  private readonly projectService = inject(ProjectService);
  private readonly router = inject(Router);

  readonly projects = signal<Project[]>([]);
  readonly loading = signal(false);

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.loading.set(true);
    this.projectService.list().subscribe({
      next: (projects) => {
        this.projects.set(projects);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  onCreate(request: CreateProjectRequest): void {
    this.projectService.create(request).subscribe((created) => {
      this.projects.update((current) => [...current, created]);
    });
  }

  onDelete(id: number): void {
    this.projectService.delete(id).subscribe(() => {
      this.projects.update((current) => current.filter((project) => project.id !== id));
    });
  }

  onViewTasks(projectId: number): void {
    this.router.navigate(['/projects', projectId, 'tasks']);
  }
}
