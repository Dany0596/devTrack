import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TimeEntryService } from './time-entry.service';
import { TimeEntry } from './time-entry.model';
import { environment } from '../../../environments/environment';

describe('TimeEntryService', () => {
  let service: TimeEntryService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/time-entries`;

  const sampleEntry: TimeEntry = {
    id: 1,
    taskId: 3,
    date: '2026-09-24',
    hours: 2.5,
    notes: 'Pairing session'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TimeEntryService]
    });

    service = TestBed.inject(TimeEntryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lists all entries via GET /time-entries', () => {
    service.list().subscribe((entries) => {
      expect(entries).toEqual([sampleEntry]);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([sampleEntry]);
  });

  it('lists entries scoped to a task via GET /tasks/:id/time-entries', () => {
    service.listByTask(3).subscribe((entries) => {
      expect(entries).toEqual([sampleEntry]);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/tasks/3/time-entries`);
    expect(req.request.method).toBe('GET');
    req.flush([sampleEntry]);
  });

  it('creates an entry via POST /time-entries', () => {
    service.create({ taskId: 3, date: '2026-09-24', hours: 2.5, notes: 'Pairing session' }).subscribe((entry) => {
      expect(entry).toEqual(sampleEntry);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    req.flush(sampleEntry);
  });

  it('deletes an entry via DELETE /time-entries/:id', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
