import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProjectService } from './project.service';
import { Project } from './project.model';
import { environment } from '../../../environments/environment';

describe('ProjectService', () => {
  let service: ProjectService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/projects`;

  const sampleProject: Project = {
    id: 1,
    name: 'Website Revamp',
    description: 'Redesign the marketing site',
    status: 'PLANNED'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProjectService]
    });

    service = TestBed.inject(ProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lists projects via GET /projects', () => {
    service.list().subscribe((projects) => {
      expect(projects).toEqual([sampleProject]);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([sampleProject]);
  });

  it('creates a project via POST /projects', () => {
    service.create({ name: 'Website Revamp', description: 'Redesign the marketing site' }).subscribe((project) => {
      expect(project).toEqual(sampleProject);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ name: 'Website Revamp', description: 'Redesign the marketing site' });
    req.flush(sampleProject);
  });

  it('updates a project via PUT /projects/:id', () => {
    service.update(1, { name: 'New name', description: null, status: 'ACTIVE' }).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush({ ...sampleProject, name: 'New name', status: 'ACTIVE' });
  });

  it('deletes a project via DELETE /projects/:id', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
