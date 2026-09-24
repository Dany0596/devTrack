import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TimeEntry } from '../../time-entry.model';

@Component({
  selector: 'app-time-entry-card',
  standalone: true,
  templateUrl: './time-entry-card.component.html',
  styleUrl: './time-entry-card.component.scss'
})
export class TimeEntryCardComponent {
  @Input({ required: true }) entry!: TimeEntry;

  @Output() delete = new EventEmitter<number>();

  onDelete(): void {
    this.delete.emit(this.entry.id);
  }
}
