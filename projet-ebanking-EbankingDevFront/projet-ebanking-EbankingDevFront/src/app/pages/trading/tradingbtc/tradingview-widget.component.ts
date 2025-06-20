import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-tradingview-widget',
  templateUrl: './tradingview-widget.component.html',
  styleUrls: ['./tradingview-widget.component.css'],
  standalone: false
})
export class TradingViewWidgetComponent implements AfterViewInit {

  @ViewChild('container', { static: true }) container!: ElementRef;

  ngAfterViewInit(): void {
    const script = document.createElement('script');
    script.src = 'https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js';
    script.async = true;
    script.innerHTML = `
      {
         "autosize": true,
      "symbol": "BINANCE:BTCUSDT",
      "interval": "5",
      "timezone": "Etc/UTC",
      "theme": "light",
      "style": "1",
      "locale": "en",
      "allow_symbol_change": true,
      "support_host": "https://www.tradingview.com"
      }`;

    this.container.nativeElement.appendChild(script);
  }
}
