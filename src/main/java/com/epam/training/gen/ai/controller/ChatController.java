package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.Repository.KnowledgeRepository;
import com.epam.training.gen.ai.client.RagService;
import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.model.DeploymentModel;
import com.epam.training.gen.ai.model.Movie;
import com.epam.training.gen.ai.prompt.PromptService;
import com.epam.training.gen.ai.spi.MovieRecommendations;
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
    @Autowired
    MovieRecommendations movieRecommendations;
    @Autowired
    RagService ragService;
    @Autowired
    KnowledgeRepository knowledgeRepository;

    @PostMapping("/userPrompt/openAi")
    public List<ChatResponse> getResponseFromOpenAi(@RequestBody ChatRequest request){
        return promptService.getResponse(request.getInputPrompt(), DeploymentModel.OPEN_AI);
    }

    @PostMapping("/userPrompt/amazon")
    public List<ChatResponse> getResponseFromAmazon(@RequestBody ChatRequest request){
        return promptService.getResponse(request.getInputPrompt(), DeploymentModel.AMAZON);
    }

    @PostMapping("/userPrompt/recommendedMovies")
    public List<Movie> queryMovieRecommendations(@RequestBody ChatRequest request){
        return movieRecommendations.getRecommendations(request.getInputPrompt());
    }

    @PostMapping("/userPrompt/moviesData")
    public String addMovieData(@RequestBody List<Movie> movies){
        movieRecommendations.addMovies(movies);
        return "Movies Added Successfully";
    }

    @PostMapping("/userPrompt/rag/addData")
    public String addMovieData(@RequestBody ChatRequest request){
        knowledgeRepository.addData(request.getInputPrompt());
        return "Added external data successfully";
    }

    @PostMapping("/userPrompt/rag/response")
    public String queryRecommendations(@RequestBody ChatRequest request){
        return ragService.getQueryResponse(request.getInputPrompt());
    }

}
