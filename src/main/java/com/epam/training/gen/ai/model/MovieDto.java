package com.epam.training.gen.ai.model;

import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordData;
import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordKey;
import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordVector;
import com.microsoft.semantickernel.data.vectorstorage.definition.DistanceFunction;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieDto {

    @VectorStoreRecordKey
    private final String id;

    @VectorStoreRecordData
    private final String movieDescription;

    @VectorStoreRecordVector(dimensions = 1536, distanceFunction = DistanceFunction.COSINE_DISTANCE)
    private final List<Float> embedding;

    public MovieDto() {
        this(null,null,null);
    }

    public MovieDto(String id, String movieDescription, List<Float> embedding) {
        this.id = id;
        this.movieDescription = movieDescription;
        this.embedding = embedding;
    }

}
