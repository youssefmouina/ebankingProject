import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompteResDTO } from '../model/dto/CompteResDTO';





@Injectable({
  providedIn: 'root'
})
export class ChatService {


  constructor(private http: HttpClient) {

   }

  private baseUrl = 'http://localhost:8080';

  sendMessage(message: string): Observable<ChatResponse> {
    return this.http.post<ChatResponse>(`${this.baseUrl}/api/chatbot`, { message }, { withCredentials: true });
  }


}
interface ChatResponse {
  text: string;
  comptes?: CompteResDTO[];
  type: 'text' | 'comptes';
}






