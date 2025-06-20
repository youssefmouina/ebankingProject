package com.ebank.ebanking2.controller.chatController;

import com.ebank.ebanking2.Service.chatService.ChatService;
import com.ebank.ebanking2.model.dto.chatdto.ChatRequest;
import com.ebank.ebanking2.model.dto.chatdto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
//    @PreAuthorize("hasRole('CLIENT') or hasRole('EMPLOYEE')")
    @PostMapping
    public ChatResponse getChatResponse(@RequestBody ChatRequest chatRequest){
        System.out.println("entered");
        return new ChatResponse(chatService.getChatResponse(chatRequest));
    }
}
