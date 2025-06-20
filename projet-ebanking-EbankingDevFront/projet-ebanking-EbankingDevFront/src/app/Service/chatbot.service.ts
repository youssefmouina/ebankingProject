import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ChatRequest } from '../model/dto/ChatRequest';
import { ChatResponse } from "../model/dto/ChatResponse";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
  })


export class chatbotService{
    private chatbotUrl = "http://localhost:8080/api/chat";
    constructor(private http: HttpClient) {}


    sendChat(request: ChatRequest): Observable<ChatResponse> {
        return this.http.post<ChatResponse>(this.chatbotUrl, request, { withCredentials: true })
    }

}
