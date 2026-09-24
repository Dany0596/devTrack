import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateTimeEntryRequest, TimeEntry, UpdateTimeEntryRequest } from './time-entry.model';

@Injectable({ providedIn: 'root' })
export class TimeEntryService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/time-entries`;

  list(): Observable<TimeEntry[]> {
    return this.http.get<TimeEntry[]>(this.baseUrl);
  }

  listByTask(taskId: number): Observable<TimeEntry[]> {
    return this.http.get<TimeEntry[]>(`${environment.apiBaseUrl}/tasks/${taskId}/time-entries`);
  }

  getById(id: number): Observable<TimeEntry> {
    return this.http.get<TimeEntry>(`${this.baseUrl}/${id}`);
  }

  create(request: CreateTimeEntryRequest): Observable<TimeEntry> {
    return this.http.post<TimeEntry>(this.baseUrl, request);
  }

  update(id: number, request: UpdateTimeEntryRequest): Observable<TimeEntry> {
    return this.http.put<TimeEntry>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
