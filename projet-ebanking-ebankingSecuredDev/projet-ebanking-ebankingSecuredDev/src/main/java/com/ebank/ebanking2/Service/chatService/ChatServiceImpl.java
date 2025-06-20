package com.ebank.ebanking2.Service.chatService;


import com.ebank.ebanking2.model.dto.chatdto.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private Assistant assistant;

    @Override
    public String getChatResponse(ChatRequest chatRequest) {
        System.out.println("entered 2 - userId: " + chatRequest.userId() + ", question: " + chatRequest.question());
        String response = assistant.chat(chatRequest.userId(), chatRequest.question(), chatRequest.userId());
        System.out.println("AI response: " + response);
        return response;
    }



//    public String getChatResponseSimple(ChatRequest chatRequest) {
//        List<ChatMessage> messages = new ArrayList<>();
//        //messages.add(SystemMessage.systemMessage("response in frensh"));
//        messages.add(UserMessage.userMessage(chatRequest.question()));
//        var model = OpenAiChatModel.builder()
//                .apiKey("demo")
//                .modelName(OpenAiChatModelName.GPT_4_O_MINI)
//                .build();
//        return model.generate(messages).content().text();
//    }
}
