package com.predera.dmml.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.predera.dmml.service.PredictionFeedbackDTO;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionDTO {

    private String id;

    @NotNull
    private String index;

    @NotNull
    private String type;

    @NotNull
    @JsonProperty("model_id")
    private Long modelId;

    @NotNull
    @JsonProperty("model_name")
    private String modelName;

    @NotNull
    private String project;

    @NotNull
    private String version;

    private String uid;

    @NotNull
    @JsonProperty("prediction_input")
    private Map<String, Float> predictionInput;

    @NotNull
    @JsonProperty("prediction_date")
    private Instant predictionDate;

    @NotNull
    @JsonProperty("prediction_output")
    private float predictionOutput;

    @JsonProperty("prediction_reasons")
    private Map<String, Float> predictionReasons;

    private PredictionFeedbackDTO feedback;

    public PredictionDTO() {
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
//        map.put("index", index);
//        map.put("type", type);
        map.put("model_id", modelId);
        map.put("model_name", modelName);
        map.put("project", project);
        map.put("version", version);
        map.put("uid", uid);
        map.put("prediction_input", predictionInput);
//        for (String key : predictionInput.keySet()) {
//            map.put(key, predictionInput.get(key));
//        }
        map.put("prediction_date", predictionDate);
        map.put("prediction_output", predictionOutput);
        map.put("prediction_reasons", predictionReasons);
        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Float> getPredictionInput() {
        return predictionInput;
    }

    public void setPredictionInput(Map<String, Float> predictionInput) {
        this.predictionInput = predictionInput;
    }

    public Instant getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(Instant predictionDate) {
        this.predictionDate = predictionDate;
    }

    public float getPredictionOutput() {
        return predictionOutput;
    }

    public void setPredictionOutput(float predictionOutput) {
        this.predictionOutput = predictionOutput;
    }

    public Map<String, Float> getPredictionReasons() {
        return predictionReasons;
    }

    public void setPredictionReasons(Map<String, Float> predictionReasons) {
        this.predictionReasons = predictionReasons;
    }

    public PredictionFeedbackDTO getFeedback() {
        return feedback;
    }

    public void setFeedback(PredictionFeedbackDTO feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictionDTO that = (PredictionDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(index, that.index) &&
            Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, type);
    }

    @Override
    public String toString() {
        return "PredictionDTO{" +
            "id='" + id + '\'' +
            ", index='" + index + '\'' +
            ", type='" + type + '\'' +
            ", modelId='" + modelId + '\'' +
            ", modelName='" + modelName + '\'' +
            ", project='" + project + '\'' +
            ", version='" + version + '\'' +
            ", predictionDate=" + predictionDate +
            ", predictionOutput=" + predictionOutput +
            '}';
    }
}
