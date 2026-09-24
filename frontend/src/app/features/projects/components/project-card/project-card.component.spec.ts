import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProjectCardComponent } from './project-card.component';
import { Project } from '../../project.model';

describe('ProjectCardComponent', () => {
  let fixture: ComponentFixture<ProjectCardComponent>;
  let component: ProjectCardComponent;

  const sampleProject: Project = {
    id: 1,
    name: 'Website Revamp',
    description: 'Redesign the marketing site',
    status: 'PLANNED'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [ProjectCardComponent] });
    fixture = TestBed.createComponent(ProjectCardComponent);
    component = fixture.componentInstance;
    component.project = sampleProject;
    fixture.detectChanges();
  });

  it('emits delete with the project id', () => {
    spyOn(component.delete, 'emit');

    component.onDelete();

    expect(component.delete.emit).toHaveBeenCalledWith(1);
  });

  it('emits viewTasks with the project id', () => {
    spyOn(component.viewTasks, 'emit');

    component.onViewTasks();

    expect(component.viewTasks.emit).toHaveBeenCalledWith(1);
  });
});
