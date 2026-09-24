import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskFormComponent } from './task-form.component';

describe('TaskFormComponent', () => {
  let fixture: ComponentFixture<TaskFormComponent>;
  let component: TaskFormComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TaskFormComponent] });
    fixture = TestBed.createComponent(TaskFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('defaults the priority to MEDIUM', () => {
    expect(component.form.controls.priority.value).toBe('MEDIUM');
  });

  it('does not emit create when the title is blank', () => {
    spyOn(component.create, 'emit');

    component.onSubmit();

    expect(component.create.emit).not.toHaveBeenCalled();
    expect(component.form.controls.title.touched).toBeTrue();
  });

  it('emits create with the form values and resets to MEDIUM priority', () => {
    spyOn(component.create, 'emit');
    component.form.setValue({ title: 'Design schema', description: 'Draft the ER diagram', priority: 'URGENT' });

    component.onSubmit();

    expect(component.create.emit).toHaveBeenCalledWith({
      title: 'Design schema',
      description: 'Draft the ER diagram',
      priority: 'URGENT'
    });
    expect(component.form.controls.priority.value).toBe('MEDIUM');
  });
});
