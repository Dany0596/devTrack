import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { TimeEntryListComponent } from './time-entry-list.component';
import { TimeEntryService } from '../../time-entry.service';
import { TimeEntry } from '../../time-entry.model';

describe('TimeEntryListComponent', () => {
  let fixture: ComponentFixture<TimeEntryListComponent>;
  let component: TimeEntryListComponent;
  let timeEntryServiceSpy: jasmine.SpyObj<TimeEntryService>;

  const sampleEntry: TimeEntry = {
    id: 1,
    taskId: 3,
    date: '2026-09-24',
    hours: 2.5,
    notes: 'Pairing session'
  };

  function configure(paramMap: Record<string, string>): void {
    timeEntryServiceSpy = jasmine.createSpyObj('TimeEntryService', ['list', 'listByTask', 'create', 'delete']);
    timeEntryServiceSpy.list.and.returnValue(of([sampleEntry]));
    timeEntryServiceSpy.listByTask.and.returnValue(of([sampleEntry]));

    TestBed.configureTestingModule({
      imports: [TimeEntryListComponent],
      providers: [
        provideRouter([]),
        { provide: TimeEntryService, useValue: timeEntryServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap(paramMap) } }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TimeEntryListComponent);
    component = fixture.componentInstance;
  }

  it('loads entries scoped to the task when a taskId route param is present', () => {
    configure({ taskId: '3' });
    fixture.detectChanges();

    expect(timeEntryServiceSpy.listByTask).toHaveBeenCalledWith(3);
    expect(timeEntryServiceSpy.list).not.toHaveBeenCalled();
    expect(component.entries()).toEqual([sampleEntry]);
    expect(component.totalHours()).toBe(2.5);
  });

  it('loads all entries when no taskId route param is present', () => {
    configure({});
    fixture.detectChanges();

    expect(timeEntryServiceSpy.list).toHaveBeenCalled();
    expect(timeEntryServiceSpy.listByTask).not.toHaveBeenCalled();
  });

  it('recomputes the total after a successful delete', () => {
    configure({ taskId: '3' });
    timeEntryServiceSpy.delete.and.returnValue(of(void 0));
    fixture.detectChanges();

    component.onDelete(1);

    expect(timeEntryServiceSpy.delete).toHaveBeenCalledWith(1);
    expect(component.entries()).toEqual([]);
    expect(component.totalHours()).toBe(0);
  });
});
