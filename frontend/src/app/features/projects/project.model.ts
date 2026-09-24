export type ProjectStatus = 'PLANNED' | 'ACTIVE' | 'ON_HOLD' | 'COMPLETED';

export interface Project {
  id: number;
  name: string;
  description: string | null;
  status: ProjectStatus;
}

export interface CreateProjectRequest {
  name: string;
  description: string | null;
}

export interface UpdateProjectRequest {
  name: string;
  description: string | null;
  status: ProjectStatus;
}
