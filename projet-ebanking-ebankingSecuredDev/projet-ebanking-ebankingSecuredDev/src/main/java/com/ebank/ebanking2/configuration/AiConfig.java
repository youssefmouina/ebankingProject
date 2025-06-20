package com.ebank.ebanking2.configuration;

import com.ebank.ebanking2.Service.BankingTools;
import com.ebank.ebanking2.Service.chatService.Assistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AiConfig {

    @Bean
    public Assistant assistant(BankingTools tools){
        System.out.println("entered3");
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel())
                .chatMemoryProvider(memoryId-> MessageWindowChatMemory.withMaxMessages(10)) // store latest  10 messages
                .tools(tools)
                .build();
    }

    @Bean
    public ChatLanguageModel chatLanguageModel(){
        return OpenAiChatModel.builder()
                .apiKey("demo")
                .modelName(OpenAiChatModelName.GPT_4_O_MINI)
                .build();
    }
}
