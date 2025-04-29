package com.epam.training.gen.ai.client;
import com.epam.training.gen.ai.rag.RagClient;
import org.springframework.stereotype.Service;

@Service
public class RagService {
    // This class is a service that interacts with the RagClient to get responses
    private final RagClient ragClient;

    public RagService(RagClient ragClient) {
        this.ragClient = ragClient;
    }

    public String getQueryResponse(String input) {
        return ragClient.getResponse(input);
    }
}
