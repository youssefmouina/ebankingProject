import { Component, OnInit } from '@angular/core';
import { ClientResDTO } from '../../model/ClientResDTO';
import { ClientService } from '../../services/client.service';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-clients',
  standalone: false,
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.css'
})
export class ClientsComponent  {
  editingClientId: number | null = null;
editedClient!: ClientResDTO;
client!:ClientResDTO;


saveClient(id:string) {
   const edit = {
    username: this.editedClient.username,
    email: this.editedClient.email,
    phone: this.editedClient.phone,
    job: this.editedClient.job
  };

  const headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });


  // Here you could call an update API, then:

  this.http.updateclient(id,edit,headers).subscribe((ref:any)=>{
    console.log(ref);
      this.getall();
  });

  this.cancelEdit();


}

cancelEdit() {
  this.editingClientId = null;

}

  constructor(private http:ClientService){
this.getall();
console.log(this.clients);

  }
  clients: any[] = [

  ];

  validateClient(client: ClientResDTO,id:number) {
    client.valid = true;
    this.http.validateclient(id.toString()).subscribe((ref:any)=>console.log("true"));

  }

  showDetails(client: ClientResDTO) {
    console.log('Client details:', client);
  }

  showComptes(client: ClientResDTO) {
    console.log('Client comptes:', client.comptes);
  }

  showInvoices(client: ClientResDTO) {
    console.log('Client invoices:', client.invoices);
  }

  modifyClient(id:string,client: ClientResDTO) {
     this.editingClientId = client.id;
  this.editedClient = { ...client };
  }
  getall(){
    this.http.getallclients().subscribe((ref:any)=>
    {
      this.clients = ref.sort((a: { id: number; }, b: { id: number; }) => a.id - b.id);
      console.log(this.clients);
    })
  }
}
