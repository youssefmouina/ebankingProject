// import { Component, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
// import { ChatService } from '../../../Service/chat.service';
// import { CompteResDTO } from '../../../model/dto/CompteResDTO';
// import { AuthService } from '../../../Service/Auth.service';
//
//
// @Component({
//   selector: 'app-chatbot',
//   standalone: false,
//   templateUrl: './chatbot.component.html',
//   styleUrls: ['./chatbot.component.css']  // ← fixed here
// })
// export class ChatbotComponent implements AfterViewChecked {
//   @ViewChild('chatMessages') private messagesContainer!: ElementRef;
//
//   userMessage: string = '';
//   isChatOpen: boolean = false;
//   messages: { text: string; isUser: boolean }[] = [
//     { text: 'Bonjour ! Comment puis-je vous aider aujourd\'hui ?', isUser: false }
//   ];
//
//   // clientId: string = sessionStorage.getItem('userid') ?? '';
//   clientId: string = localStorage.getItem('userid') ?? '';
//   comptes : CompteResDTO[]=[];
//   constructor(
//     private chatService: ChatService, private authService: AuthService
//   ){
//
//   }
//
//
//
//   toggleChat() {
//     this.isChatOpen = !this.isChatOpen;
//     if (this.isChatOpen) {
//       setTimeout(() => {
//         this.scrollToBottom();
//       }, 100);
//     }
//   }
//
//   sendMessage() {
//     if (this.userMessage.trim()) {
//       // Add user message
//       this.messages.push({ text: this.userMessage, isUser: true });
//
//       this.chatService.sendMessage(this.userMessage).subscribe({
//         next: (response) => {
//           if (response.type === 'comptes' && response.comptes) {
//             console.log("yes");
//             this.comptes = response.comptes;
//             this.messages.push({
//               text: 'Voici vos comptes :',
//               isUser: false
//             });
//           } else {
//             this.messages.push({
//               text: response.text,
//               isUser: false
//             });
//           }
//         },
//         error: (err) => {
//           if (err.status === 401 || err.status === 403) {
//             this.authService.logout();
//           } else {
//             console.error('Erreur lors du chargement des comptes', err)
//           }
//         }
//       });
//
//       this.userMessage = '';
//     }
//   }
//
//
//
//   ngAfterViewChecked() {
//     if (this.isChatOpen) {
//       this.scrollToBottom();
//     }
//   }
//
//   private scrollToBottom(): void {
//     try {
//       this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
//     } catch(err) { }
//   }
// }
