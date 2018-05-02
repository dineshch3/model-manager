package com.predera.dmml.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionFeedbackDTO {

    @NotNull
    @JsonProperty("feedback_value")
    private float feedbackValue;

    @NotNull
    @JsonProperty("feedback_date")
    private Instant feedbackDate;

    @NotNull
    @JsonProperty("feedback_user")
    private String feedbackUser;

    public PredictionFeedbackDTO() {
    }

    public float getFeedbackValue() {
        return feedbackValue;
    }

    public void setFeedbackValue(float feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    public Instant getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Instant feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(String feedbackUser) {
        this.feedbackUser = feedbackUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictionFeedbackDTO that = (PredictionFeedbackDTO) o;
        return Float.compare(that.feedbackValue, feedbackValue) == 0 &&
            Objects.equals(feedbackDate, that.feedbackDate) &&
            Objects.equals(feedbackUser, that.feedbackUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackValue, feedbackDate, feedbackUser);
    }

    @Override
    public String toString() {
        return "PredictionFeedbackDTO{" +
            "feedbackValue=" + feedbackValue +
            ", feedbackDate=" + feedbackDate +
            ", feedbackUser='" + feedbackUser + '\'' +
            '}';
    }
}
