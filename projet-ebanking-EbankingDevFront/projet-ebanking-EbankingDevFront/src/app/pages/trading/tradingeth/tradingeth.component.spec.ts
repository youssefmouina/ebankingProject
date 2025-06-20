import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TradingethComponent } from './tradingeth.component';

describe('TradingethComponent', () => {
  let component: TradingethComponent;
  let fixture: ComponentFixture<TradingethComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TradingethComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TradingethComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
