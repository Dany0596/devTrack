import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TimeEntryCardComponent } from './time-entry-card.component';
import { TimeEntry } from '../../time-entry.model';

describe('TimeEntryCardComponent', () => {
  let fixture: ComponentFixture<TimeEntryCardComponent>;
  let component: TimeEntryCardComponent;

  const sampleEntry: TimeEntry = {
    id: 1,
    taskId: 3,
    date: '2026-09-24',
    hours: 2.5,
    notes: 'Pairing session'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TimeEntryCardComponent] });
    fixture = TestBed.createComponent(TimeEntryCardComponent);
    component = fixture.componentInstance;
    component.entry = sampleEntry;
    fixture.detectChanges();
  });

  it('emits delete with the entry id', () => {
    spyOn(component.delete, 'emit');

    component.onDelete();

    expect(component.delete.emit).toHaveBeenCalledWith(1);
  });
});
