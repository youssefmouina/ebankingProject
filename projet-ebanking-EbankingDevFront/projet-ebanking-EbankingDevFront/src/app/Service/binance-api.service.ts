import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CryptoModel } from '../model/dto/CryptoModel';


@Injectable({ providedIn: 'root' })
export class BinanceWebSocketService {
  private socket!: WebSocket;
  public cryptoData$ = new BehaviorSubject<CryptoModel[]>([]);

  connect() {
    this.socket = new WebSocket('wss://stream.binance.com:9443/ws');

    this.socket.onopen = () => {
      // Subscribe to BTCUSDT and ETHUSDT ticker streams
      this.socket.send(JSON.stringify({
        method: "SUBSCRIBE",
        params: ["btcusdt@ticker", "ethusdt@ticker"],
        id: 1
      }));
    };

    this.socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.e === '24hrTicker') {
        this.updateCryptoData(data);
      }
    };
  }

  private updateCryptoData(wsData: any) {
    const currentData = this.cryptoData$.value;
    const updatedData = currentData.filter(c => c.symbol !== wsData.s.toUpperCase());

    updatedData.push({
      symbol: wsData.s.toUpperCase(), // "BTCUSDT"
      price: wsData.c,               // Current price
      change: (parseFloat(wsData.c) - parseFloat(wsData.o)).toFixed(2), // Price change
      percent: wsData.P,             // Percentage change
      volume: wsData.v               // 24h volume
    });

    this.cryptoData$.next(updatedData);
  }

  disconnect() {
    if (this.socket) {
      this.socket.close();
    }
  }

}
