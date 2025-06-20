import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentAccountStatsComponent } from './current-account-stats.component';

describe('CurrentAccountStatsComponent', () => {
  let component: CurrentAccountStatsComponent;
  let fixture: ComponentFixture<CurrentAccountStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CurrentAccountStatsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentAccountStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
