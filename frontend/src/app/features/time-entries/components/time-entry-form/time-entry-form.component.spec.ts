import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TimeEntryFormComponent } from './time-entry-form.component';

describe('TimeEntryFormComponent', () => {
  let fixture: ComponentFixture<TimeEntryFormComponent>;
  let component: TimeEntryFormComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TimeEntryFormComponent] });
    fixture = TestBed.createComponent(TimeEntryFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('does not emit create when hours is zero', () => {
    spyOn(component.create, 'emit');
    component.form.controls.hours.setValue(0);

    component.onSubmit();

    expect(component.create.emit).not.toHaveBeenCalled();
    expect(component.form.controls.hours.touched).toBeTrue();
  });

  it('emits create with the form values when valid', () => {
    spyOn(component.create, 'emit');
    component.form.setValue({ date: '2026-09-24', hours: 3.5, notes: 'Pairing session' });

    component.onSubmit();

    expect(component.create.emit).toHaveBeenCalledWith({
      date: '2026-09-24',
      hours: 3.5,
      notes: 'Pairing session'
    });
  });

  it('emits null notes when the notes field is left blank', () => {
    spyOn(component.create, 'emit');
    component.form.setValue({ date: '2026-09-24', hours: 1, notes: '' });

    component.onSubmit();

    expect(component.create.emit).toHaveBeenCalledWith({ date: '2026-09-24', hours: 1, notes: null });
  });
});
