import { Component } from '@angular/core';

@Component({
  selector: 'invoices',
  standalone: false,
  templateUrl: './invoices.component.html',
  styleUrl: './invoices.component.css'
})
export class InvoicesComponent {


  eauElectricite = new Map<string, string>([
    ["assets/invoices/eau&electricite/onee.jpg", "ONEE"],
    ["assets/invoices/eau&electricite/amendis.png", "AMENDIS"],
    ["assets/invoices/eau&electricite/radeel.png", "RADEEL"],
    ["assets/invoices/eau&electricite/radeet.png", "RADEET"],
    ["assets/invoices/eau&electricite/radeeta.png", "RADEETA"],
    ["assets/invoices/eau&electricite/radeef.png", "RADEEF"],
    ["assets/invoices/eau&electricite/rak.jpg", "RAK"],
    ["assets/invoices/eau&electricite/srm.jpeg", "SRM"],
  ]);
  
  taxes = new Map<string, string>([
    ["assets/invoices/taxes/ancfcc.jpeg", "ANCFCC"],
    ["assets/invoices/taxes/ANP.jpg", "ANP"],
    ["assets/invoices/taxes/cnss.jpg", "CNSS"],
    ["assets/invoices/taxes/dgi.png", "DGI"],
    ["assets/invoices/taxes/ompic.png", "OMPIC"],
    ["assets/invoices/taxes/onssa.png", "ONSSA"],
    ["assets/invoices/taxes/tgr.jpeg", "TGR"],
    ["assets/invoices/taxes/portnet.jpeg", "PORTNET"],
  ]);
  
  internet = new Map<string, string>([
    ["assets/invoices/telecom/inwi.png", "INWI"],
    ["assets/invoices/telecom/maroctelecom.png", "MAROCTELECOM"],
    ["assets/invoices/telecom/orange.png", "ORANGE"],
  ]);
  
  

  selectedName : string =""; //providerName

  showModal : boolean = false;


  
  openModal(invoiceName:string){
    this.selectedName=invoiceName;
    console.log(this.selectedName);
    this.showModal=true;
  }

  closeModal(){
    this.showModal=false;
  }

  
    
  

}
