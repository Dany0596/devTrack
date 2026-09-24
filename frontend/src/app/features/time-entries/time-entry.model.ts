export interface TimeEntry {
  id: number;
  taskId: number;
  date: string;
  hours: number;
  notes: string | null;
}

export interface CreateTimeEntryRequest {
  taskId: number;
  date: string;
  hours: number;
  notes: string | null;
}

export interface UpdateTimeEntryRequest {
  date: string;
  hours: number;
  notes: string | null;
}
