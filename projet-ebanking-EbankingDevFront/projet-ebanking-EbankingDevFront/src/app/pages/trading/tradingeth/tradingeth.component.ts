import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'app-tradingeth',
  imports: [],
  templateUrl: './tradingeth.component.html',
  styleUrl: './tradingeth.component.css'
})
export class TradingethComponent implements AfterViewInit{


  @ViewChild('container', { static: true }) container!: ElementRef;

  ngAfterViewInit(): void {
    const script = document.createElement('script');
    script.src = 'https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js';
    script.async = true;
    script.innerHTML = `
      {
        "autosize": true,
  "symbol": "BINANCE:ETHUSDT",
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
