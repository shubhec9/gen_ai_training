package com.epam.training.gen.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "client.openai")
public class OpenAIClientProperties {
    String key;
    String endpoint;
    String deploymentName;
}
