import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskService } from './task.service';
import { Task } from './task.model';
import { environment } from '../../../environments/environment';

describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/tasks`;

  const sampleTask: Task = {
    id: 1,
    title: 'Design schema',
    description: null,
    status: 'TODO',
    priority: 'HIGH',
    projectId: 5
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TaskService]
    });

    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lists all tasks via GET /tasks', () => {
    service.list().subscribe((tasks) => {
      expect(tasks).toEqual([sampleTask]);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([sampleTask]);
  });

  it('lists tasks scoped to a project via GET /projects/:id/tasks', () => {
    service.listByProject(5).subscribe((tasks) => {
      expect(tasks).toEqual([sampleTask]);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/projects/5/tasks`);
    expect(req.request.method).toBe('GET');
    req.flush([sampleTask]);
  });

  it('creates a task via POST /tasks', () => {
    service.create({ title: 'Design schema', description: null, priority: 'HIGH', projectId: 5 }).subscribe((task) => {
      expect(task).toEqual(sampleTask);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    req.flush(sampleTask);
  });

  it('deletes a task via DELETE /tasks/:id', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
