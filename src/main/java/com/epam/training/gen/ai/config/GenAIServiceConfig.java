package com.epam.training.gen.ai.config;

import com.epam.training.gen.ai.client.AIModelClientFactory;
import com.epam.training.gen.ai.client.SemanticKernelService;
import com.epam.training.gen.ai.prompt.PromptService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAIServiceConfig {

    @Bean
    public PromptService promptService(SemanticKernelService semanticKernelService) {
        return new PromptService(semanticKernelService);
    }

    @Bean
    public SemanticKernelService semanticKernelService(ChatHistory chatHistory, AIModelClientFactory AIModelClientFactory) {
        return new SemanticKernelService(chatHistory, AIModelClientFactory);
    }

}
