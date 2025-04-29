package com.epam.training.gen.ai.config;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.client.AmazonClient;
import com.epam.training.gen.ai.spi.AIClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonClientConfig {

    @Bean("amazonChatCompletionService")
    public ChatCompletionService chatCompletionService(CommonClientProperties clientProperties,
                                                       OpenAIAsyncClient openAIAsyncClient) {

        return OpenAIChatCompletion.builder()
                .withModelId(clientProperties.getDeploymentModels().get("amazon"))
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean("amazonAIClient")
    public AIClient amazonAIClient(@Qualifier("amazonChatCompletionService") ChatCompletionService amazonChatCompletionService,
                                   Kernel kernel,
                                   ChatHistory chatHistory,
                                   InvocationContext invocationContext
    ) {
        return new AmazonClient(amazonChatCompletionService, invocationContext, chatHistory, kernel);
    }
}
