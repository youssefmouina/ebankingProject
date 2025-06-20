import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecuModalComponent } from './recu-modal.component';

describe('RecuModalComponent', () => {
  let component: RecuModalComponent;
  let fixture: ComponentFixture<RecuModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecuModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecuModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
