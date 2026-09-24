import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { ProjectListComponent } from './project-list.component';
import { ProjectService } from '../../project.service';
import { Project } from '../../project.model';

describe('ProjectListComponent', () => {
  let fixture: ComponentFixture<ProjectListComponent>;
  let component: ProjectListComponent;
  let projectServiceSpy: jasmine.SpyObj<ProjectService>;

  const sampleProject: Project = {
    id: 1,
    name: 'Website Revamp',
    description: 'Redesign the marketing site',
    status: 'PLANNED'
  };

  beforeEach(async () => {
    projectServiceSpy = jasmine.createSpyObj('ProjectService', ['list', 'create', 'delete']);
    projectServiceSpy.list.and.returnValue(of([sampleProject]));

    await TestBed.configureTestingModule({
      imports: [ProjectListComponent],
      providers: [provideRouter([]), { provide: ProjectService, useValue: projectServiceSpy }]
    }).compileComponents();

    fixture = TestBed.createComponent(ProjectListComponent);
    component = fixture.componentInstance;
  });

  it('loads projects on init', () => {
    fixture.detectChanges();

    expect(projectServiceSpy.list).toHaveBeenCalled();
    expect(component.projects()).toEqual([sampleProject]);
  });

  it('removes a project after a successful delete', () => {
    projectServiceSpy.delete.and.returnValue(of(void 0));
    fixture.detectChanges();

    component.onDelete(1);

    expect(projectServiceSpy.delete).toHaveBeenCalledWith(1);
    expect(component.projects()).toEqual([]);
  });

  it('navigates to the project tasks page on onViewTasks', () => {
    fixture.detectChanges();
    const router = TestBed.inject(Router);
    const navigateSpy = spyOn(router, 'navigate');

    component.onViewTasks(1);

    expect(navigateSpy).toHaveBeenCalledWith(['/projects', 1, 'tasks']);
  });
});
