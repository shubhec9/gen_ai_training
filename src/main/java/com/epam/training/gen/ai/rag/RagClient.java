package com.epam.training.gen.ai.rag;

import com.epam.training.gen.ai.Repository.KnowledgeRepository;
import com.epam.training.gen.ai.model.KnowledgeStoreDto;
import com.epam.training.gen.ai.utility.LoadTemplates;
import com.google.gson.Gson;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RagClient {

    private final ChatCompletionService chatCompletionService;
    private final ChatHistory queryChatHistory;
    private final ChatHistory responseChatHistory;
    private final Kernel kernel;
    private final Gson gson;
    private final InvocationContext invocationContext;
    private final KnowledgeRepository knowledgeRepository;


    public RagClient(ChatCompletionService chatCompletionService, Kernel kernel, Gson gson, InvocationContext invocationContext, KnowledgeRepository knowledgeRepository) {
        this.chatCompletionService = chatCompletionService;
        this.knowledgeRepository = knowledgeRepository;
        this.queryChatHistory = new ChatHistory();
        this.responseChatHistory = new ChatHistory();
        this.kernel = kernel;
        this.gson = gson;
        this.invocationContext = invocationContext;
    }


    public String getResponse(String input) {

        String systemPrompt = LoadTemplates.getSystemMessage("query_expansion_template.yaml");
        log.info("Original Query: {}", input);
        queryChatHistory.addSystemMessage(systemPrompt);
        queryChatHistory.addUserMessage(input);

        List<ChatMessageContent<?>> content =
                chatCompletionService.getChatMessageContentsAsync(queryChatHistory, kernel, invocationContext).block();

        List<String> responses = Objects.requireNonNull(content).stream().map(String::valueOf).collect(Collectors.toList());
        responses.add(input);
        String query = String.join(",", responses);

        // Generate embeddings for the user query
        List<KnowledgeStoreDto> embeddingsGeneratedForUserQuery = getEmbeddingsGeneratedForUserQuery(query);

        // Generate augmented response using the embeddings
        String augmentedResponse = getAugmentedResponse(embeddingsGeneratedForUserQuery,input);
        log.info("Final Response: {}", augmentedResponse);
        return augmentedResponse;
    }

    private List<KnowledgeStoreDto> getEmbeddingsGeneratedForUserQuery(String input) {

        List<KnowledgeStoreDto> knowledgeStoreDtos = knowledgeRepository.queryData(input);
        log.info("Responses retrieved from DB: {}", knowledgeStoreDtos.stream().map(KnowledgeStoreDto::getDescription).collect(Collectors.joining("/n")));
        return knowledgeStoreDtos;
    }

    private String getAugmentedResponse(List<KnowledgeStoreDto> knowledgeStoreDtos, String input) {

        String responseSystemPrompt = LoadTemplates.getSystemMessage("query_response_template.yaml");
        responseChatHistory.addSystemMessage(responseSystemPrompt);

        String information = knowledgeStoreDtos.stream().map(KnowledgeStoreDto::getDescription).collect(Collectors.joining(","));

        String promptWithFormat =
                String.format("By using this input: %s, " +
                        "Summarize this information: %s", input, information);

        responseChatHistory.addUserMessage(promptWithFormat);
        List<ChatMessageContent<?>> content = chatCompletionService.getChatMessageContentsAsync(responseChatHistory, kernel, invocationContext).block();
        List<String> responses = Objects.requireNonNull(content).stream().map(String::valueOf).toList();
        return String.join(",", responses);
    }


}
