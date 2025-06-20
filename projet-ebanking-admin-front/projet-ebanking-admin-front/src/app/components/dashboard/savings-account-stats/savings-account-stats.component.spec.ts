import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavingsAccountStatsComponent } from './savings-account-stats.component';

describe('SavingsAccountStatsComponent', () => {
  let component: SavingsAccountStatsComponent;
  let fixture: ComponentFixture<SavingsAccountStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SavingsAccountStatsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SavingsAccountStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
