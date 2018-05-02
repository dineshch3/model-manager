package com.predera.dmml.service;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.predera.dmml.ModelmanagerApp;
import com.predera.dmml.config.ApplicationProperties;
import com.predera.dmml.service.dto.PredictionDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelmanagerApp.class)
public class DataServiceTest {

    @Autowired
    private RestHighLevelClient client;

    private DataService dataService;
    
    private ApplicationProperties applicationProperties;

    @Before
    public void setup() {
        this.dataService = new DataService(client, applicationProperties);
    }

    @Test
    public void testSaveFeedback() throws IOException {
        PredictionDTO prediction = new PredictionDTO();
        prediction.setIndex("predictions");
        prediction.setType("prediction");
        prediction.setModelId(951L);
        prediction.setModelName("test-model");
        prediction.setProject("test-project");
        prediction.setVersion("1");
        prediction.setUid("1024");
        Map<String, Float> predictionInput = new HashMap<>();
        predictionInput.put("age", 25f);
        predictionInput.put("gender", 1f);
        prediction.setPredictionInput(predictionInput);
        prediction.setPredictionDate(Instant.now());
        prediction.setPredictionOutput(1f);
        Map<String, Float> predictionReasons = predictionInput;
        prediction.setPredictionReasons(predictionReasons);

        PredictionDTO savedPrediction = dataService.savePrediction(prediction);

        Assert.assertNotNull(savedPrediction);
        Assert.assertNotNull(savedPrediction.getId());
    }

}
