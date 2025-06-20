import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CCourantResDTO} from '../model/dto/CCouurantResDTO';
import {RechargeDTO} from '../model/dto/RechargeDTO';
import { RechargeResDTO } from '../model/dto/RechargeResDTO';





@Injectable({
  providedIn: 'root'
})
export class RechargeService {

  private baseUrl = 'http://localhost:8080/api';


  constructor(private http: HttpClient) { }


  // getComptesCourantsAutorises(clientId: number): Observable<CCourantResDTO[]> {
  //   // On appelle l'endpoint que tu as en backend (à adapter si besoin)
  //   const url = `${this.baseUrl}/comptes/client/${clientId}/ccourant/ACTIF`;
  //   return this.http.get<CCourantResDTO[]>(url);
  // }

  effectuerRecharge(recharge: RechargeDTO): Observable<RechargeResDTO> {
    const url = `${this.baseUrl}/recharges/effectuer`;
    return this.http.post<RechargeResDTO>(url, recharge, { withCredentials: true });
  }
}
