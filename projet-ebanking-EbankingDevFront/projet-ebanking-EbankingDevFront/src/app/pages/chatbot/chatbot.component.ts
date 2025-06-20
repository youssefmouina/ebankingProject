import { Component } from '@angular/core';
import { chatbotService } from '../../Service/chatbot.service';
import { ChatRequest } from '../../model/dto/ChatRequest';
import {AuthService} from '../../Service/Auth.service';

interface Message {
  sender: 'user' | 'bot';
  text: string;
}

@Component({
  selector: 'chatbot',
  standalone:false,
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent {

  constructor(private chatbotService: chatbotService, private authService: AuthService) {}

  isOpen = false;
  clientId = localStorage.getItem('userid');

  clientQuestion = "";
  messages: Message[] = [];

  toggleChat() {
    this.isOpen = !this.isOpen;
  }

  closeChat() {
    this.isOpen = false;
  }

  send() {
    if (!this.clientQuestion.trim()) return; // ignore empty

    // Add user message to chat
    this.messages.push({ sender: 'user', text: this.clientQuestion });

    const chatRequest : ChatRequest = {
      question: this.clientQuestion,
      userId: Number(this.clientId)
    };

    this.clientQuestion = ""; // clear input

    this.chatbotService.sendChat(chatRequest).subscribe({
      next: (chatResponse) => {
        // Add chatbot response to chat
        this.messages.push({ sender: 'bot', text: chatResponse.response });
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        }
        else{
          console.error("error in chat:", err);
          this.messages.push({ sender: 'bot', text: "Sorry, something went wrong." });
        }
      }
    });
  }
}
