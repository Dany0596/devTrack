import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { TaskListComponent } from './task-list.component';
import { TaskService } from '../../task.service';
import { Task } from '../../task.model';

describe('TaskListComponent', () => {
  let fixture: ComponentFixture<TaskListComponent>;
  let component: TaskListComponent;
  let taskServiceSpy: jasmine.SpyObj<TaskService>;

  const sampleTask: Task = {
    id: 1,
    title: 'Design schema',
    description: null,
    status: 'TODO',
    priority: 'HIGH',
    projectId: 5
  };

  function configure(paramMap: Record<string, string>): void {
    taskServiceSpy = jasmine.createSpyObj('TaskService', ['list', 'listByProject', 'create', 'delete']);
    taskServiceSpy.list.and.returnValue(of([sampleTask]));
    taskServiceSpy.listByProject.and.returnValue(of([sampleTask]));

    TestBed.configureTestingModule({
      imports: [TaskListComponent],
      providers: [
        provideRouter([]),
        { provide: TaskService, useValue: taskServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap(paramMap) } }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskListComponent);
    component = fixture.componentInstance;
  }

  it('loads tasks scoped to the project when a projectId route param is present', () => {
    configure({ projectId: '5' });
    fixture.detectChanges();

    expect(taskServiceSpy.listByProject).toHaveBeenCalledWith(5);
    expect(taskServiceSpy.list).not.toHaveBeenCalled();
    expect(component.tasks()).toEqual([sampleTask]);
  });

  it('loads all tasks when no projectId route param is present', () => {
    configure({});
    fixture.detectChanges();

    expect(taskServiceSpy.list).toHaveBeenCalled();
    expect(taskServiceSpy.listByProject).not.toHaveBeenCalled();
  });

  it('removes a task from the list after a successful delete', () => {
    configure({ projectId: '5' });
    taskServiceSpy.delete.and.returnValue(of(void 0));
    fixture.detectChanges();

    component.onDelete(1);

    expect(taskServiceSpy.delete).toHaveBeenCalledWith(1);
    expect(component.tasks()).toEqual([]);
  });
});
