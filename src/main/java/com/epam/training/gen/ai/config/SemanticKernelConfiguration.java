package com.epam.training.gen.ai.config;

import com.epam.training.gen.ai.plugin.AgeCalculatorPlugin;
import com.epam.training.gen.ai.plugin.CurrentDateTimeCalculatorPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


    @Bean
    public ChatHistory chatHistory() {
        return new ChatHistory();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     */
    @Bean
    public Kernel kernel() {
        KernelPlugin timePlugin = KernelPluginFactory.createFromObject(new CurrentDateTimeCalculatorPlugin(),"TimePlugin");
        KernelPlugin agePlugin = KernelPluginFactory.createFromObject(new AgeCalculatorPlugin(),"AgePlugin");
        return Kernel.builder().withPlugin(timePlugin).withPlugin(agePlugin).build();

    }
}
