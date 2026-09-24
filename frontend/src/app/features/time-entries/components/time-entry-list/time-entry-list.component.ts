import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TimeEntryCardComponent } from '../time-entry-card/time-entry-card.component';
import { TimeEntryFormComponent, TimeEntryFormValue } from '../time-entry-form/time-entry-form.component';
import { TimeEntryService } from '../../time-entry.service';
import { TimeEntry } from '../../time-entry.model';

@Component({
  selector: 'app-time-entry-list',
  standalone: true,
  imports: [TimeEntryCardComponent, TimeEntryFormComponent, RouterLink],
  templateUrl: './time-entry-list.component.html',
  styleUrl: './time-entry-list.component.scss'
})
export class TimeEntryListComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly timeEntryService = inject(TimeEntryService);

  readonly entries = signal<TimeEntry[]>([]);
  readonly loading = signal(false);

  /** The owning task's id, or null when showing time entries across all tasks. */
  readonly taskId = signal<number | null>(null);

  readonly totalHours = signal(0);

  ngOnInit(): void {
    const rawTaskId = this.route.snapshot.paramMap.get('taskId');
    this.taskId.set(rawTaskId ? Number(rawTaskId) : null);
    this.loadEntries();
  }

  loadEntries(): void {
    this.loading.set(true);
    const taskId = this.taskId();
    const request$ = taskId === null ? this.timeEntryService.list() : this.timeEntryService.listByTask(taskId);

    request$.subscribe({
      next: (entries) => {
        this.entries.set(entries);
        this.recomputeTotal();
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  onCreate(value: TimeEntryFormValue): void {
    const taskId = this.taskId();
    if (taskId === null) {
      return;
    }

    this.timeEntryService.create({ ...value, taskId }).subscribe((created) => {
      this.entries.update((current) => [...current, created]);
      this.recomputeTotal();
    });
  }

  onDelete(id: number): void {
    this.timeEntryService.delete(id).subscribe(() => {
      this.entries.update((current) => current.filter((entry) => entry.id !== id));
      this.recomputeTotal();
    });
  }

  private recomputeTotal(): void {
    const total = this.entries().reduce((sum, entry) => sum + entry.hours, 0);
    this.totalHours.set(total);
  }
}
