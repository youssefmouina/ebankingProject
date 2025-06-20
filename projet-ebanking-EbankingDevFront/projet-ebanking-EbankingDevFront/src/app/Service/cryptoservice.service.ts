import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CryptoserviceService {

  constructor(private http:HttpClient) { }
  post(achatdto:any,rib:string){
    const params: any = {
      "rib": rib
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.post(`http://localhost:8080/crypto?rib=${rib}`,achatdto,  // Serialize payload to JSON
      { headers, withCredentials: true });

  }
  postvendre(achatdto:any,rib:string){
    const params: any = {
      "rib": rib
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.post(`http://localhost:8080/crypto/vendre?rib=${rib}`,achatdto,  // Serialize payload to JSON
      { headers, withCredentials: true });

  }
}
