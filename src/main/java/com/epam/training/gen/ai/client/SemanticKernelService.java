package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.model.ChatResponse;
import com.google.gson.Gson;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SemanticKernelService {

  @Autowired
  private final ChatCompletionService chatCompletionService;
  @Autowired
  private final InvocationContext invocationContext;
  @Autowired
  private final ChatHistory chatHistory;
  @Autowired
  private final Kernel kernel;
  private final Gson gson;

  public SemanticKernelService(
          ChatCompletionService chatCompletionService,
          InvocationContext invocationContext,
          ChatHistory chatHistory,
          Kernel kernel) {
    this.chatCompletionService = chatCompletionService;
    this.invocationContext = invocationContext;
    this.chatHistory = chatHistory;
    this.kernel = kernel;
    this.gson = new Gson();
  }

  public List<ChatResponse> getResponse(String query) {

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
    List<ChatMessageContent<?>> queryResponse =
        chatCompletionService
            .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
            .block();
    List<ChatResponse> responses = new ArrayList<>();

      assert queryResponse != null;
      for (ChatMessageContent<?> result : queryResponse) {
      chatHistory.addMessage(result);
      responses.add(gson.fromJson(String.valueOf(result), ChatResponse.class));
    }

    return responses;
  }
}
