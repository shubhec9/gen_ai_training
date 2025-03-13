package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.prompt.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    PromptService promptService;

    @PostMapping("/userPrompt")
    public List<ChatResponse> getResponse(@RequestBody ChatRequest request){
        return promptService.getResponse(request.getInputPrompt());
    }

}
