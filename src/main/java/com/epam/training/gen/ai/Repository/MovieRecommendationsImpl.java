package com.epam.training.gen.ai.Repository;

import com.epam.training.gen.ai.model.Movie;
import com.epam.training.gen.ai.model.MovieDto;
import com.epam.training.gen.ai.spi.MovieRecommendations;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.data.jdbc.JDBCVectorStore;
import com.microsoft.semantickernel.data.jdbc.JDBCVectorStoreRecordCollectionOptions;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResult;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResults;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import com.microsoft.semantickernel.data.vectorstorage.options.VectorSearchOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class MovieRecommendationsImpl implements MovieRecommendations {

    private final VectorStoreRecordCollection<String, MovieDto> recordCollection;
    private final OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService;

    public MovieRecommendationsImpl(JDBCVectorStore jdbcVectorStore,
                                             OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService) {
        this.recordCollection = jdbcVectorStore.getCollection("movie_recommendations",
                JDBCVectorStoreRecordCollectionOptions.<MovieDto>builder()
                        .withRecordClass(MovieDto.class)
                        .build()).createCollectionIfNotExistsAsync().block();
        this.openAITextEmbeddingGenerationService = openAITextEmbeddingGenerationService;
    }


    @Override
    public void addMovies(List<Movie> movies) {

        movies
                .forEach(movie -> {
                    List<Float> embeddingVector =
                            Objects.requireNonNull(openAITextEmbeddingGenerationService
                                    .generateEmbeddingAsync(movie.getDescription()).block()).getVector();
                    MovieDto movieDto = new MovieDto(movie.getId(), movie.getDescription(), embeddingVector);
                    String key = recordCollection.upsertAsync(movieDto, null).block();

                    log.info("Successfully Inserted document with key{}", key);
                });
    }

    @Override
    public List<Movie> getRecommendations(String userPreference) {

        List<Float> userQuery =
                Objects.requireNonNull(openAITextEmbeddingGenerationService.generateEmbeddingAsync(userPreference)
                                .block())
                        .getVector();

        VectorSearchResults<MovieDto> results = recordCollection.searchAsync(userQuery, VectorSearchOptions.builder()
                .build()).block();
        assert results != null;
        return results.getResults()
                .stream()
                .peek(this::logRecord)
                .map(VectorSearchResult::getRecord)
                .map(re -> new Movie(re.getId(), re.getMovieDescription()))
                .collect(Collectors.toList());
    }


    private void logRecord(VectorSearchResult<MovieDto> vectorSearchResult) {
        if (vectorSearchResult == null) {
            log.warn("VectorSearchResult is null");
            return;
        }
        log.info("Search result with score: {}. id: {}, description: {}",
                vectorSearchResult.getScore(), vectorSearchResult.getRecord().getId(),
                vectorSearchResult.getRecord().getMovieDescription());
    }


}
