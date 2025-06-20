package com.ebank.ebanking2.Service.chatService;

import com.ebank.ebanking2.model.dto.InvoiceResDTO;
import com.ebank.ebanking2.model.dto.chatdto.ChatRequest;


public interface ChatService {
    String getChatResponse(ChatRequest chatRequest);
}
