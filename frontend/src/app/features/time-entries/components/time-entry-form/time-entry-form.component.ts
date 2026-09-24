import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

export interface TimeEntryFormValue {
  date: string;
  hours: number;
  notes: string | null;
}

@Component({
  selector: 'app-time-entry-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './time-entry-form.component.html',
  styleUrl: './time-entry-form.component.scss'
})
export class TimeEntryFormComponent {
  private readonly formBuilder = new FormBuilder();

  @Output() create = new EventEmitter<TimeEntryFormValue>();

  readonly form = this.formBuilder.nonNullable.group({
    date: [new Date().toISOString().substring(0, 10), [Validators.required]],
    hours: [1, [Validators.required, Validators.min(0.25)]],
    notes: ['', [Validators.maxLength(2000)]]
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.create.emit({ date: value.date, hours: value.hours, notes: value.notes || null });
    this.form.patchValue({ notes: '' });
  }
}
