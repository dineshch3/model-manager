package com.predera.dmml.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionResponseDTO {

    @NotNull
    @JsonProperty("query_id")
    private long queryId;

    @NotNull
    @JsonProperty("default")
    private Boolean defaultFlag;

    @NotNull
	private PredictionResponseOutputDTO output;

    public PredictionResponseDTO() {
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    public Boolean getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public PredictionResponseOutputDTO getOutput() {
        return output;
    }

    public void setOutput(PredictionResponseOutputDTO output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "PredictionResponseDto{" +
            "queryId=" + queryId +
            ", defaultFlag=" + defaultFlag +
            ", output=" + output +
            '}';
    }

    public class PredictionResponseOutputDTO {
        @NotNull
        private float result;
        @NotNull
        private Map<String, Float> reasons;

        public PredictionResponseOutputDTO() {
        }

        public float getResult() {
            return result;
        }

        public void setResult(float result) {
            this.result = result;
        }

        public Map<String, Float> getReasons() {
            return reasons;
        }

        public void setReasons(Map<String, Float> reasons) {
            this.reasons = reasons;
        }

        @Override
        public String toString() {
            return "PredictionResponseOutputDTO{" +
                "result=" + result +
                ", reasons=" + reasons +
                '}';
        }
    }
}
