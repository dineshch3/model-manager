package com.predera.dmml.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Prediction entity. This class is used in PredictionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /predictions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PredictionCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private InstantFilter timestamp;

    private LongFilter queryId;

    private StringFilter outputClass;

    private BooleanFilter feedbackFlag;

    private StringFilter feedbackClass;

    private FloatFilter feedbackValue;

    private InstantFilter feedbackTimestamp;

    private StringFilter feedbackUser;

    private StringFilter defaultDesc;

    private BooleanFilter errorFlag;

    private StringFilter errorDesc;

    private StringFilter errorCause;

    private BooleanFilter defaultFlag;

    private FloatFilter outputValue;

    private LongFilter modelId;

    public PredictionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public LongFilter getQueryId() {
        return queryId;
    }

    public void setQueryId(LongFilter queryId) {
        this.queryId = queryId;
    }

    public StringFilter getOutputClass() {
        return outputClass;
    }

    public void setOutputClass(StringFilter outputClass) {
        this.outputClass = outputClass;
    }

    public BooleanFilter getFeedbackFlag() {
        return feedbackFlag;
    }

    public void setFeedbackFlag(BooleanFilter feedbackFlag) {
        this.feedbackFlag = feedbackFlag;
    }

    public StringFilter getFeedbackClass() {
        return feedbackClass;
    }

    public void setFeedbackClass(StringFilter feedbackClass) {
        this.feedbackClass = feedbackClass;
    }

    public FloatFilter getFeedbackValue() {
        return feedbackValue;
    }

    public void setFeedbackValue(FloatFilter feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    public InstantFilter getFeedbackTimestamp() {
        return feedbackTimestamp;
    }

    public void setFeedbackTimestamp(InstantFilter feedbackTimestamp) {
        this.feedbackTimestamp = feedbackTimestamp;
    }

    public StringFilter getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(StringFilter feedbackUser) {
        this.feedbackUser = feedbackUser;
    }

    public StringFilter getDefaultDesc() {
        return defaultDesc;
    }

    public void setDefaultDesc(StringFilter defaultDesc) {
        this.defaultDesc = defaultDesc;
    }

    public BooleanFilter getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(BooleanFilter errorFlag) {
        this.errorFlag = errorFlag;
    }

    public StringFilter getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(StringFilter errorDesc) {
        this.errorDesc = errorDesc;
    }

    public StringFilter getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(StringFilter errorCause) {
        this.errorCause = errorCause;
    }

    public BooleanFilter getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(BooleanFilter defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public FloatFilter getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(FloatFilter outputValue) {
        this.outputValue = outputValue;
    }

    public LongFilter getModelId() {
        return modelId;
    }

    public void setModelId(LongFilter modelId) {
        this.modelId = modelId;
    }

    @Override
    public String toString() {
        return "PredictionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (queryId != null ? "queryId=" + queryId + ", " : "") +
                (outputClass != null ? "outputClass=" + outputClass + ", " : "") +
                (feedbackFlag != null ? "feedbackFlag=" + feedbackFlag + ", " : "") +
                (feedbackClass != null ? "feedbackClass=" + feedbackClass + ", " : "") +
                (feedbackValue != null ? "feedbackValue=" + feedbackValue + ", " : "") +
                (feedbackTimestamp != null ? "feedbackTimestamp=" + feedbackTimestamp + ", " : "") +
                (feedbackUser != null ? "feedbackUser=" + feedbackUser + ", " : "") +
                (defaultDesc != null ? "defaultDesc=" + defaultDesc + ", " : "") +
                (errorFlag != null ? "errorFlag=" + errorFlag + ", " : "") +
                (errorDesc != null ? "errorDesc=" + errorDesc + ", " : "") +
                (errorCause != null ? "errorCause=" + errorCause + ", " : "") +
                (defaultFlag != null ? "defaultFlag=" + defaultFlag + ", " : "") +
                (outputValue != null ? "outputValue=" + outputValue + ", " : "") +
                (modelId != null ? "modelId=" + modelId + ", " : "") +
            "}";
    }

}
