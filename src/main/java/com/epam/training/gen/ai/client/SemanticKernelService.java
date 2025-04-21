package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.model.DeploymentModel;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SemanticKernelService {

  @Autowired
  private final ChatHistory chatHistory;

  private final AIModelClientFactory AIModelClientFactory;

  public SemanticKernelService(ChatHistory chatHistory, AIModelClientFactory AIModelClientFactory) {
    this.chatHistory = chatHistory;
    this.AIModelClientFactory = AIModelClientFactory;
  }

  public List<ChatResponse> getResponse(String query, DeploymentModel deploymentModel) {
// Add system message
    chatHistory.addSystemMessage(
            "You are a helpful assistant"
    );
    String promptWithFormat =
        String.format(
            """
                For any question asked,
                 - You should write the answer.
                 - The answer should be in below JSON format:
                    {
                    "inputPrompt": "Question being asked,
                    "response": Response from AI model assistant"
                    }
                 - The response should not contain any special characters
                 - please make sure the response is properly closed with json braces, so that I can serialize the text into json object
                 - Please limit the number of words to 40
                Here is the question you should answer: %s
                """,
            query);
    chatHistory.addUserMessage(promptWithFormat);

    return AIModelClientFactory.getAIClientProvider(deploymentModel).getChatResponse(promptWithFormat);
  }
}
