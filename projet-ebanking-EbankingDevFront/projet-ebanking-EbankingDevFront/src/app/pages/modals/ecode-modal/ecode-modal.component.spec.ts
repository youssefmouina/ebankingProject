import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EcodeModalComponent } from './ecode-modal.component';

describe('EcodeModalComponent', () => {
  let component: EcodeModalComponent;
  let fixture: ComponentFixture<EcodeModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EcodeModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EcodeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
