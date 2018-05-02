package com.predera.dmml.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * A Prediction.
 */
@Entity
@Table(name = "prediction")
public class Prediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Lob
    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "query_id")
    private Long queryId;

    @Column(name = "output_class")
    private String outputClass;

    @Column(name = "feedback_flag")
    private Boolean feedbackFlag;

    @Column(name = "feedback_class")
    private String feedbackClass;

    @Column(name = "feedback_value")
    private Float feedbackValue;

    @Column(name = "feedback_timestamp")
    private Instant feedbackTimestamp;

    @Column(name = "feedback_user")
    private String feedbackUser;

    @Column(name = "default_desc")
    private String defaultDesc;

    @Column(name = "error_flag")
    private Boolean errorFlag;

    @Column(name = "error_desc")
    private String errorDesc;

    @Column(name = "error_cause")
    private String errorCause;

    @Column(name = "default_flag")
    private Boolean defaultFlag;

    @NotNull
    @Lob
    @Column(name = "reasons", nullable = false)
    private String reasons;

    @NotNull
    @Column(name = "output_value", nullable = false)
    private Float outputValue;

    @ManyToOne(optional = false)
    @NotNull
    private Model model;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Prediction timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getInput() {
        return input;
    }

    public Prediction input(String input) {
        this.input = input;
        return this;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Long getQueryId() {
        return queryId;
    }

    public Prediction queryId(Long queryId) {
        this.queryId = queryId;
        return this;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getOutputClass() {
        return outputClass;
    }

    public Prediction outputClass(String outputClass) {
        this.outputClass = outputClass;
        return this;
    }

    public void setOutputClass(String outputClass) {
        this.outputClass = outputClass;
    }

    public Boolean isFeedbackFlag() {
        return feedbackFlag;
    }

    public Prediction feedbackFlag(Boolean feedbackFlag) {
        this.feedbackFlag = feedbackFlag;
        return this;
    }

    public void setFeedbackFlag(Boolean feedbackFlag) {
        this.feedbackFlag = feedbackFlag;
    }

    public String getFeedbackClass() {
        return feedbackClass;
    }

    public Prediction feedbackClass(String feedbackClass) {
        this.feedbackClass = feedbackClass;
        return this;
    }

    public void setFeedbackClass(String feedbackClass) {
        this.feedbackClass = feedbackClass;
    }

    public Float getFeedbackValue() {
        return feedbackValue;
    }

    public Prediction feedbackValue(Float feedbackValue) {
        this.feedbackValue = feedbackValue;
        return this;
    }

    public void setFeedbackValue(Float feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    public Instant getFeedbackTimestamp() {
        return feedbackTimestamp;
    }

    public Prediction feedbackTimestamp(Instant feedbackTimestamp) {
        this.feedbackTimestamp = feedbackTimestamp;
        return this;
    }

    public void setFeedbackTimestamp(Instant feedbackTimestamp) {
        this.feedbackTimestamp = feedbackTimestamp;
    }

    public String getFeedbackUser() {
        return feedbackUser;
    }

    public Prediction feedbackUser(String feedbackUser) {
        this.feedbackUser = feedbackUser;
        return this;
    }

    public void setFeedbackUser(String feedbackUser) {
        this.feedbackUser = feedbackUser;
    }

    public String getDefaultDesc() {
        return defaultDesc;
    }

    public Prediction defaultDesc(String defaultDesc) {
        this.defaultDesc = defaultDesc;
        return this;
    }

    public void setDefaultDesc(String defaultDesc) {
        this.defaultDesc = defaultDesc;
    }

    public Boolean isErrorFlag() {
        return errorFlag;
    }

    public Prediction errorFlag(Boolean errorFlag) {
        this.errorFlag = errorFlag;
        return this;
    }

    public void setErrorFlag(Boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public Prediction errorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
        return this;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public Prediction errorCause(String errorCause) {
        this.errorCause = errorCause;
        return this;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public Boolean isDefaultFlag() {
        return defaultFlag;
    }

    public Prediction defaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
        return this;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getReasons() {
        return reasons;
    }

    public Prediction reasons(String reasons) {
        this.reasons = reasons;
        return this;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public Float getOutputValue() {
        return outputValue;
    }

    public Prediction outputValue(Float outputValue) {
        this.outputValue = outputValue;
        return this;
    }

    public void setOutputValue(Float outputValue) {
        this.outputValue = outputValue;
    }

    public Model getModel() {
        return model;
    }

    public Prediction model(Model model) {
        this.model = model;
        return this;
    }

    public void setModel(Model model) {
        this.model = model;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prediction prediction = (Prediction) o;
        if (prediction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prediction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prediction{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", input='" + getInput() + "'" +
            ", queryId=" + getQueryId() +
            ", outputClass='" + getOutputClass() + "'" +
            ", feedbackFlag='" + isFeedbackFlag() + "'" +
            ", feedbackClass='" + getFeedbackClass() + "'" +
            ", feedbackValue=" + getFeedbackValue() +
            ", feedbackTimestamp='" + getFeedbackTimestamp() + "'" +
            ", feedbackUser='" + getFeedbackUser() + "'" +
            ", defaultDesc='" + getDefaultDesc() + "'" +
            ", errorFlag='" + isErrorFlag() + "'" +
            ", errorDesc='" + getErrorDesc() + "'" +
            ", errorCause='" + getErrorCause() + "'" +
            ", defaultFlag='" + isDefaultFlag() + "'" +
            ", reasons='" + getReasons() + "'" +
            ", outputValue=" + getOutputValue() +
            "}";
    }
}
