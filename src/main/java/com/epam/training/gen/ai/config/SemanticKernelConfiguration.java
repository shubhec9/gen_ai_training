package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "prompt.openai.completion")
public class SemanticKernelConfiguration {
    private double temperature;
    private int maxTokens;
    private double presencePenalty;
    private double frequencyPenalty;
    private double topP;
    /**
     * Creates an {@link InvocationContext} bean with default prompt execution settings.
     *
     * @return an instance of {@link InvocationContext}
     */

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(temperature)
                        .withMaxTokens(maxTokens)
                        .withFrequencyPenalty(frequencyPenalty)
                        .withPresencePenalty(presencePenalty)
                        .withTopP(topP)
                        .build())
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();
    }

    /**
     * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
     **/

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIClientProperties openAIClientProperties,
                                                       OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(openAIClientProperties.getDeploymentName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }


    @Bean
    public ChatHistory chatHistory() {
        return new ChatHistory();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     */

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }


}
