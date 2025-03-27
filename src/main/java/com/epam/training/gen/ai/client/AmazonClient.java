package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.spi.AIClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Component
@Qualifier("amazonChatService")
public class AmazonClient implements AIClient {

    @Autowired
    @Qualifier("amazonChatCompletionService")
    private final ChatCompletionService chatCompletionService;
    @Autowired
    private final InvocationContext invocationContext;
    @Autowired
    private final ChatHistory chatHistory;
    @Autowired
    private final Kernel kernel;
    private final Gson gson;

    public AmazonClient(
            @Qualifier("amazonChatCompletionService")ChatCompletionService chatCompletionService,
            InvocationContext invocationContext, ChatHistory chatHistory,
            Kernel kernel) {
        this.chatCompletionService = chatCompletionService;
        this.invocationContext = invocationContext;
        this.chatHistory = chatHistory;
        this.kernel = kernel;
        this.gson = new Gson();
    }

    @Override
    public List<ChatResponse> getChatResponse(String prompt){
        List<ChatMessageContent<?>> queryResponse =
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                        .block();
        List<ChatResponse> responses = new ArrayList<>();

        assert queryResponse != null;
        queryResponse.stream().filter(Objects::nonNull).filter(result -> result.getAuthorRole() == AuthorRole.ASSISTANT).forEach(result -> {
            chatHistory.addAssistantMessage(String.valueOf(result));
            responses.add(getChatResponse(result));
        });
        log.info("Amazon Chat Response: {}", responses);
        return responses;
    }

    private ChatResponse getChatResponse(ChatMessageContent<?> result) {
        try {
            return gson.fromJson(removeQuotes(String.valueOf(result)), ChatResponse.class);
        } catch (JsonSyntaxException e) {
            log.error("Failed to parse JSON response: {}", result, e);
            throw new IllegalArgumentException("Invalid JSON response", e);
        }
    }

    private String removeQuotes(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("`", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }
}
