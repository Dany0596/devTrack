import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskPriority } from '../../task.model';

export interface TaskFormValue {
  title: string;
  description: string | null;
  priority: TaskPriority;
}

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './task-form.component.html',
  styleUrl: './task-form.component.scss'
})
export class TaskFormComponent {
  private readonly formBuilder = new FormBuilder();

  @Output() create = new EventEmitter<TaskFormValue>();

  readonly priorities: TaskPriority[] = ['LOW', 'MEDIUM', 'HIGH', 'URGENT'];

  readonly form = this.formBuilder.nonNullable.group({
    title: ['', [Validators.required, Validators.maxLength(200)]],
    description: ['', [Validators.maxLength(2000)]],
    priority: ['MEDIUM' as TaskPriority, [Validators.required]]
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.create.emit({ title: value.title, description: value.description || null, priority: value.priority });
    this.form.reset({ title: '', description: '', priority: 'MEDIUM' });
  }
}
