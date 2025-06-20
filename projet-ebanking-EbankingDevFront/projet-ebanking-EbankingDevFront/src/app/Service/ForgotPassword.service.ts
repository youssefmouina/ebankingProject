import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import { MaybeClientDTO } from '../model/dto/MaybeClientDTO';
import { MaybeClientWithEmailTokenDTO } from '../model/dto/MaybeClientWithEmailTokenDTO';
import { CheckRecoveryTokenDTO } from '../model/dto/CheckRecoveryTokenDTO';
import { ChangePasswordDTO } from '../model/dto/ChangePasswordDTO';

@Injectable({
  providedIn: 'root'
})
export class ForgotPassword {
  private readonly apiUrl = 'http://localhost:8080/api/clients';

  constructor(private http: HttpClient) {}

  generateRecoveryPasswordToken(email: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/registry/emailSend/recoveryToken`, {email});
  }

  checkRecoveryToken(dto: CheckRecoveryTokenDTO): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/registry/checkRecoveryToken`, dto);
  }

  changePassword(dto: ChangePasswordDTO): Observable<boolean> {
    return this.http.put<boolean>(`${this.apiUrl}/registry/changePassword`, dto);
  }
}
