import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'projects' },
  {
    path: 'projects',
    loadComponent: () =>
      import('./features/projects/components/project-list/project-list.component').then(
        (m) => m.ProjectListComponent
      )
  },
  {
    path: 'projects/:projectId/tasks',
    loadComponent: () =>
      import('./features/tasks/components/task-list/task-list.component').then(
        (m) => m.TaskListComponent
      )
  },
  {
    path: 'tasks',
    loadComponent: () =>
      import('./features/tasks/components/task-list/task-list.component').then(
        (m) => m.TaskListComponent
      )
  },
  {
    path: 'tasks/:taskId/time-entries',
    loadComponent: () =>
      import('./features/time-entries/components/time-entry-list/time-entry-list.component').then(
        (m) => m.TimeEntryListComponent
      )
  },
  {
    path: 'time-entries',
    loadComponent: () =>
      import('./features/time-entries/components/time-entry-list/time-entry-list.component').then(
        (m) => m.TimeEntryListComponent
      )
  },
  { path: '**', redirectTo: 'projects' }
];
