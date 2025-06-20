import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http :HttpClient) { }
  validateclient(id:string){
    return this.http.post(`http://localhost:8080/api/clients/validate/${id}`,{}, { withCredentials: true })
  }
  getallclients(){
   return this.http.get(`http://localhost:8080/api/clients`, { withCredentials: true });
  }
  updateclient(id:string,client:any,headers:HttpHeaders){
    return this.http.put(`http://localhost:8080/api/clients/update/client/${id}`,client, { withCredentials: true ,headers: headers});

  }
}
