import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TaskCardComponent } from '../task-card/task-card.component';
import { TaskFormComponent, TaskFormValue } from '../task-form/task-form.component';
import { TaskService } from '../../task.service';
import { Task } from '../../task.model';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [TaskCardComponent, TaskFormComponent, RouterLink],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly taskService = inject(TaskService);

  readonly tasks = signal<Task[]>([]);
  readonly loading = signal(false);

  /** The owning project's id, or null when showing tasks across all projects. */
  readonly projectId = signal<number | null>(null);

  ngOnInit(): void {
    const rawProjectId = this.route.snapshot.paramMap.get('projectId');
    this.projectId.set(rawProjectId ? Number(rawProjectId) : null);
    this.loadTasks();
  }

  loadTasks(): void {
    this.loading.set(true);
    const projectId = this.projectId();
    const request$ = projectId === null ? this.taskService.list() : this.taskService.listByProject(projectId);

    request$.subscribe({
      next: (tasks) => {
        this.tasks.set(tasks);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  onCreate(value: TaskFormValue): void {
    const projectId = this.projectId();
    if (projectId === null) {
      return;
    }

    this.taskService.create({ ...value, projectId }).subscribe((created) => {
      this.tasks.update((current) => [...current, created]);
    });
  }

  onDelete(id: number): void {
    this.taskService.delete(id).subscribe(() => {
      this.tasks.update((current) => current.filter((task) => task.id !== id));
    });
  }

  onViewTimeEntries(taskId: number): void {
    this.router.navigate(['/tasks', taskId, 'time-entries']);
  }
}
