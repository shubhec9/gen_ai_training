package com.epam.training.gen.ai.Repository;

import com.epam.training.gen.ai.model.KnowledgeStoreDto;
import com.epam.training.gen.ai.splitter.TextSplitter;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.data.jdbc.JDBCVectorStore;
import com.microsoft.semantickernel.data.jdbc.JDBCVectorStoreRecordCollectionOptions;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResult;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import com.microsoft.semantickernel.data.vectorstorage.options.VectorSearchOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * KnowledgeRepository is responsible for managing the knowledge store.
 * It allows adding data to the store and querying the store for relevant information.
 */
public class KnowledgeRepository {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRepository.class);
    private final VectorStoreRecordCollection<String, KnowledgeStoreDto> recordCollection;
    private final OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService;

    public KnowledgeRepository(JDBCVectorStore jdbcVectorStore,
                               OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService) {
        this.recordCollection = jdbcVectorStore.getCollection("knowledge_store",
                JDBCVectorStoreRecordCollectionOptions.<KnowledgeStoreDto>builder()
                        .withRecordClass(KnowledgeStoreDto.class)
                        .build()).createCollectionIfNotExistsAsync().block();
        this.openAITextEmbeddingGenerationService = openAITextEmbeddingGenerationService;
    }

    public void addData(String input) {
        List<String> splitText = TextSplitter.splitSource(input);
        IntStream.range(0, splitText.size())
                .mapToObj(index -> {
                    List<Float> vector = Objects.requireNonNull(openAITextEmbeddingGenerationService.generateEmbeddingAsync(splitText.get(index)).block()).getVector();
                    return KnowledgeStoreDto.builder().id(String.valueOf(index)).description(splitText.get(index)).embedding(vector).build();
                }).forEach(knowledgeStoreDto -> {
                    String key = recordCollection.upsertAsync(knowledgeStoreDto, null).block();
                    log.info("Successfully Inserted document with key:{}", key);
                });
    }

    public List<KnowledgeStoreDto> queryData(String input) {
        log.info("Query is being triggered with expanded query:{}", input);
        List<Float> generatedVectors = Objects.requireNonNull(openAITextEmbeddingGenerationService.generateEmbeddingAsync(input).block()).getVector();
        return Objects.requireNonNull(recordCollection.searchAsync(generatedVectors, VectorSearchOptions.builder().build()).block())
                .getResults().stream().map(VectorSearchResult::getRecord).collect(Collectors.toList());
    }
}
