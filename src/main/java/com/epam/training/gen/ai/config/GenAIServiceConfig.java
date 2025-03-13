package com.epam.training.gen.ai.config;

import com.epam.training.gen.ai.client.SemanticKernelService;
import com.epam.training.gen.ai.prompt.PromptService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
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
    public SemanticKernelService semanticKernelService(ChatCompletionService chatCompletionService,
                                                       InvocationContext invocationContext,
                                                       ChatHistory chatHistory,
                                                       Kernel kernel

    ) {
        return new SemanticKernelService(chatCompletionService, invocationContext, chatHistory, kernel);
    }

}
