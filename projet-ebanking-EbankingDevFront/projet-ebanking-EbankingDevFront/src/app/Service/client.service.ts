import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SafeResourceUrl } from '@angular/platform-browser';
import { EcodeDTO } from '../model/dto/EcodeDTO';
import { ClientResDTO } from '../model/dto/ClientResDTO';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private clientUrl = 'http://localhost:8080/api/clients';

  constructor(private http: HttpClient) {}

  checkEcodeSecurity(eCode: string): Observable<boolean> {
    const url = `${this.clientUrl}/isValid?ecode=${eCode}`;
    return this.http.get<boolean>(url, { withCredentials: true });
  }

  // envoyer token pour ecode en email
  sendEcodeTokenForVerification(clientId: number): Observable<boolean> {
    const url = `${this.clientUrl}/emailSend/eCode/token/${clientId}`;
    return this.http.post<boolean>(url,{}, { withCredentials: true },);
  }

  // sert à vérifier token entré par client pour un nouveau ecode le meme que celui envoyé en email
  verififyTokenEcodeSubmittedByClient(ecodeToken:String,clientId: number): Observable<boolean> {
    const url = `${this.clientUrl}/verifiytoken/ecode/${clientId}?ecodeToken=${ecodeToken}`;
    return this.http.post<boolean>(url,{}, { withCredentials: true },);
  }

  saveEcode(ecode: EcodeDTO): Observable<boolean> {
      const url = `${this.clientUrl}/saveEcode`;
      return this.http.post<boolean>(url, ecode, { withCredentials: true },);
  }

  // verifier ecode saisie par client pour valider operation(virement,paiement)
  checkEcodeForOperations(ecode: EcodeDTO): Observable<boolean> {
    const url = `${this.clientUrl}/verifyEcode`;
    return this.http.post<boolean>(url,ecode,{ withCredentials: true },);
  }

  getClient(clientId: number): Observable<ClientResDTO> {
    const url = `${this.clientUrl}/${clientId}`;
    return this.http.get<ClientResDTO>(url,{ withCredentials: true });
  }








}
