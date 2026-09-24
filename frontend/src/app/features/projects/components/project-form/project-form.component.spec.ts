import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProjectFormComponent } from './project-form.component';

describe('ProjectFormComponent', () => {
  let fixture: ComponentFixture<ProjectFormComponent>;
  let component: ProjectFormComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [ProjectFormComponent] });
    fixture = TestBed.createComponent(ProjectFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('does not emit create and marks the form as touched when the name is blank', () => {
    spyOn(component.create, 'emit');

    component.onSubmit();

    expect(component.create.emit).not.toHaveBeenCalled();
    expect(component.form.controls.name.touched).toBeTrue();
  });

  it('emits create with the form values and resets when the name is valid', () => {
    spyOn(component.create, 'emit');
    component.form.setValue({ name: 'Website Revamp', description: 'Redesign the marketing site' });

    component.onSubmit();

    expect(component.create.emit).toHaveBeenCalledWith({
      name: 'Website Revamp',
      description: 'Redesign the marketing site'
    });
    expect(component.form.controls.name.value).toBe('');
  });

  it('emits null description when the description field is left blank', () => {
    spyOn(component.create, 'emit');
    component.form.setValue({ name: 'Website Revamp', description: '' });

    component.onSubmit();

    expect(component.create.emit).toHaveBeenCalledWith({ name: 'Website Revamp', description: null });
  });
});
