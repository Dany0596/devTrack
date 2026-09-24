import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Project } from '../../project.model';

@Component({
  selector: 'app-project-card',
  standalone: true,
  templateUrl: './project-card.component.html',
  styleUrl: './project-card.component.scss'
})
export class ProjectCardComponent {
  @Input({ required: true }) project!: Project;

  @Output() delete = new EventEmitter<number>();
  @Output() viewTasks = new EventEmitter<number>();

  onDelete(): void {
    this.delete.emit(this.project.id);
  }

  onViewTasks(): void {
    this.viewTasks.emit(this.project.id);
  }
}
