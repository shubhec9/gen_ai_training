package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAIConfiguration {
    /**
     * Creates an {@link OpenAIAsyncClient} bean for interacting with Azure OpenAI Service asynchronously.
     *
     * @return an instance of {@link OpenAIAsyncClient}
     */

    @Bean
    public OpenAIAsyncClient openAIAsyncClient(CommonClientProperties commonClientProperties) {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(commonClientProperties.getKey()))
                .endpoint(commonClientProperties.getEndpoint())
                .buildAsyncClient();
    }
}
