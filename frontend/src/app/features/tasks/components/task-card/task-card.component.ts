import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Task } from '../../task.model';

@Component({
  selector: 'app-task-card',
  standalone: true,
  templateUrl: './task-card.component.html',
  styleUrl: './task-card.component.scss'
})
export class TaskCardComponent {
  @Input({ required: true }) task!: Task;

  @Output() delete = new EventEmitter<number>();
  @Output() viewTimeEntries = new EventEmitter<number>();

  onDelete(): void {
    this.delete.emit(this.task.id);
  }

  onViewTimeEntries(): void {
    this.viewTimeEntries.emit(this.task.id);
  }
}
