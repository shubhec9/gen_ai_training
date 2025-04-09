package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.model.DeploymentModel;
import com.epam.training.gen.ai.spi.AIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AIModelClientFactory {

    @Autowired
    private final AmazonClient amazonClient;
    @Autowired
    private final OpenAIGptClient openAIGptClient;

    public AIModelClientFactory(AmazonClient amazonClient, OpenAIGptClient openAIGptClient) {
        this.amazonClient = amazonClient;
        this.openAIGptClient = openAIGptClient;
    }

    protected AIClient getAIClientProvider(DeploymentModel modelName) {

        if (modelName == DeploymentModel.OPEN_AI) {
            return openAIGptClient;

        } else if (modelName == DeploymentModel.AMAZON) {
            return amazonClient;
        }
        throw new IllegalArgumentException();
    }
}
