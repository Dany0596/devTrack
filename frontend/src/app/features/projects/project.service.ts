import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateProjectRequest, Project, UpdateProjectRequest } from './project.model';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/projects`;

  list(): Observable<Project[]> {
    return this.http.get<Project[]>(this.baseUrl);
  }

  getById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.baseUrl}/${id}`);
  }

  create(request: CreateProjectRequest): Observable<Project> {
    return this.http.post<Project>(this.baseUrl, request);
  }

  update(id: number, request: UpdateProjectRequest): Observable<Project> {
    return this.http.put<Project>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
