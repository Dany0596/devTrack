import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateProjectRequest } from '../../project.model';

@Component({
  selector: 'app-project-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './project-form.component.html',
  styleUrl: './project-form.component.scss'
})
export class ProjectFormComponent {
  private readonly formBuilder = new FormBuilder();

  @Output() create = new EventEmitter<CreateProjectRequest>();

  readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    description: ['', [Validators.maxLength(2000)]]
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.create.emit({ name: value.name, description: value.description || null });
    this.form.reset();
  }
}
