import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskCardComponent } from './task-card.component';
import { Task } from '../../task.model';

describe('TaskCardComponent', () => {
  let fixture: ComponentFixture<TaskCardComponent>;
  let component: TaskCardComponent;

  const sampleTask: Task = {
    id: 1,
    title: 'Design schema',
    description: null,
    status: 'TODO',
    priority: 'HIGH',
    projectId: 5
  };

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TaskCardComponent] });
    fixture = TestBed.createComponent(TaskCardComponent);
    component = fixture.componentInstance;
    component.task = sampleTask;
    fixture.detectChanges();
  });

  it('emits delete with the task id', () => {
    spyOn(component.delete, 'emit');

    component.onDelete();

    expect(component.delete.emit).toHaveBeenCalledWith(1);
  });

  it('emits viewTimeEntries with the task id', () => {
    spyOn(component.viewTimeEntries, 'emit');

    component.onViewTimeEntries();

    expect(component.viewTimeEntries.emit).toHaveBeenCalledWith(1);
  });
});
