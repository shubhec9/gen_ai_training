package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.DeploymentModel;
import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.prompt.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    PromptService promptService;

    @PostMapping("/userPrompt/openAi")
    public List<ChatResponse>  getResponseFromOpenAi(@RequestBody ChatRequest request){
        return promptService.getResponse(request.getInputPrompt(), DeploymentModel.OPEN_AI);
    }

    @PostMapping("/userPrompt/amazon")
    public List<ChatResponse>  getResponseFromAmazon(@RequestBody ChatRequest request){
        return promptService.getResponse(request.getInputPrompt(), DeploymentModel.AMAZON);
    }

}
