import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TradingViewWidgetComponent } from './tradingview-widget.component';

describe('TradingviewWidgetComponent', () => {
  let component: TradingViewWidgetComponent;
  let fixture: ComponentFixture<TradingViewWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TradingViewWidgetComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TradingViewWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
