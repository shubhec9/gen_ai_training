package com.epam.training.gen.ai.model;

import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordData;
import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordKey;
import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordVector;
import com.microsoft.semantickernel.data.vectorstorage.definition.DistanceFunction;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
@Builder
public class KnowledgeStoreDto {

    @VectorStoreRecordKey
    String id;

    @VectorStoreRecordData
    String description;

    @VectorStoreRecordVector(dimensions = 1536, distanceFunction = DistanceFunction.COSINE_DISTANCE)
    List<Float> embedding;

    public KnowledgeStoreDto(String id, String description, List<Float> embedding) {
        this.id = id;
        this.description = description;
        this.embedding = embedding;
    }

    public KnowledgeStoreDto() {
        this(null,null,null);
    }
}
