import { Component } from '@angular/core';


@Component({
  selector: 'app-trading',
  templateUrl: './trading.component.html',
  styleUrl: './trading.component.css',
  standalone: false
})

export class TradingComponent {

constructor(){

 }
  ethclick:boolean=false;
  btcclick:boolean=true;
  Btc(){
    this.btcclick=true;
    this.ethclick=false;
  }
  Eth(){
     this.btcclick=false;
    this.ethclick=true;
  }


}
