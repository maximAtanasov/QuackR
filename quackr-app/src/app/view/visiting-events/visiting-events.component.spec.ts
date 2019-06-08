import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitingEventsComponent } from './visiting-events.component';

describe('VisitingEventsComponent', () => {
  let component: VisitingEventsComponent;
  let fixture: ComponentFixture<VisitingEventsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisitingEventsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitingEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
