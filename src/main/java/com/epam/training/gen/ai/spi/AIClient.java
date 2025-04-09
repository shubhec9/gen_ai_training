package com.epam.training.gen.ai.spi;

import com.epam.training.gen.ai.model.ChatResponse;

import java.util.List;

public interface AIClient {
    public List<ChatResponse> getChatResponse(String prompt);
}
