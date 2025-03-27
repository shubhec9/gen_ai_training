package com.epam.training.gen.ai.prompt;

import com.epam.training.gen.ai.client.SemanticKernelService;
import com.epam.training.gen.ai.model.DeploymentModel;
import com.epam.training.gen.ai.model.ChatResponse;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptService {

    @Autowired
    private final SemanticKernelService semanticKernelService;

    public PromptService(SemanticKernelService semanticKernelService) {
        this.semanticKernelService = semanticKernelService;
    }

    public List<ChatResponse> getResponse(String input, DeploymentModel deploymentModel){
        return semanticKernelService.getResponse(input, deploymentModel);
    }
}
