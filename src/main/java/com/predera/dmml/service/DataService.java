package com.predera.dmml.service;

import com.predera.dmml.config.ApplicationProperties;
import com.predera.dmml.domain.Model;
import com.predera.dmml.service.dto.PredictionDTO;
import com.predera.dmml.service.dto.PredictionResponseDTO;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    private final String index;
    private final String docType;

    private RestHighLevelClient client;

    public DataService(RestHighLevelClient client, ApplicationProperties applicationProperties) {
        this.client = client;

        this.index = applicationProperties.getDataService().getElasticsearch().getIndex();
        this.docType = applicationProperties.getDataService().getElasticsearch().getDocType();
    }

    public PredictionDTO savePrediction(PredictionDTO prediction) throws IOException {
        log.debug("Request to save Prediction: {}", prediction);

        IndexRequest indexRequest = new IndexRequest(prediction.getIndex(), prediction.getType());
        indexRequest.source("doc", prediction.toMap());
        IndexResponse response = client.index(indexRequest);
        log.debug("Index response: {}", response);

        prediction.setId(response.getId());
        return prediction;
    }

    @Async
    public CompletableFuture<PredictionDTO> asyncSavePrediction(Model model, Map<String, Float> jsonInput,
                                                                PredictionResponseDTO predictionResponseDTO) throws IOException {
        return asyncSavePrediction(model, jsonInput, predictionResponseDTO, null);
    }

    @Async
    public CompletableFuture<PredictionDTO> asyncSavePrediction(Model model, Map<String, Float> jsonInput,
                                                                PredictionResponseDTO predictionResponseDTO,
                                                                String uid) throws IOException {
    	PredictionDTO predictionDTO = new PredictionDTO();
        predictionDTO.setIndex(index);
        predictionDTO.setType(docType);
        predictionDTO.setModelId(model.getId());
        predictionDTO.setModelName(model.getName());
        predictionDTO.setProject(model.getProject());
        predictionDTO.setVersion(model.getVersion());
        predictionDTO.setUid(uid);
        predictionDTO.setPredictionInput(jsonInput);
        predictionDTO.setPredictionDate(Instant.now());
        predictionDTO.setPredictionOutput(predictionResponseDTO.getOutput().getResult());
        predictionDTO.setPredictionReasons(predictionResponseDTO.getOutput().getReasons());
        predictionDTO = savePrediction(predictionDTO);
        return CompletableFuture.completedFuture(predictionDTO);
    }
}
