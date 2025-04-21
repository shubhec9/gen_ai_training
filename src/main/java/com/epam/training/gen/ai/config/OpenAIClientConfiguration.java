package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.client.AmazonClient;
import com.epam.training.gen.ai.spi.AIClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIClientConfiguration {

    @Bean(name = "openAIChatCompletionService")
    public ChatCompletionService chatGptCompletionService(CommonClientProperties clientProperties,
                                                          OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(clientProperties.getDeploymentModels().get("openAi"))
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean("openAIClient")
    public AIClient openAIClient(@Qualifier("openAIChatCompletionService") ChatCompletionService openAIChatCompletionService,
                                 Kernel kernel,
                                 ChatHistory chatHistory,
                                 InvocationContext invocationContext
    ) {
        return new AmazonClient(openAIChatCompletionService, invocationContext, chatHistory, kernel);
    }

    @Bean
    public OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAITextEmbeddingGenerationService.builder()
                .withOpenAIAsyncClient(openAIAsyncClient)
                .withModelId("text-embedding-ada-002")
                .build();

    }
}
