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
 * Criteria class for the Model entity. This class is used in ModelResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /models?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ModelCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter type;

    private StringFilter algorithm;

    private StringFilter library;

    private StringFilter status;

    private StringFilter owner;

    private StringFilter modelLocation;

    private InstantFilter createdDate;

    private InstantFilter deployedDate;

    private StringFilter trainingDataset;

    private StringFilter performanceMetrics;

    private StringFilter featureSignificance;

    private StringFilter builderConfig;

    private StringFilter predictionUrl;

    private StringFilter project;

    private StringFilter version;

    private IntegerFilter replicationFactor;

    public ModelCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(StringFilter algorithm) {
        this.algorithm = algorithm;
    }

    public StringFilter getLibrary() {
        return library;
    }

    public void setLibrary(StringFilter library) {
        this.library = library;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getOwner() {
        return owner;
    }

    public void setOwner(StringFilter owner) {
        this.owner = owner;
    }

    public StringFilter getModelLocation() {
        return modelLocation;
    }

    public void setModelLocation(StringFilter modelLocation) {
        this.modelLocation = modelLocation;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getDeployedDate() {
        return deployedDate;
    }

    public void setDeployedDate(InstantFilter deployedDate) {
        this.deployedDate = deployedDate;
    }

    public StringFilter getTrainingDataset() {
        return trainingDataset;
    }

    public void setTrainingDataset(StringFilter trainingDataset) {
        this.trainingDataset = trainingDataset;
    }

    public StringFilter getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(StringFilter performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public StringFilter getFeatureSignificance() {
        return featureSignificance;
    }

    public void setFeatureSignificance(StringFilter featureSignificance) {
        this.featureSignificance = featureSignificance;
    }

    public StringFilter getBuilderConfig() {
        return builderConfig;
    }

    public void setBuilderConfig(StringFilter builderConfig) {
        this.builderConfig = builderConfig;
    }

    public StringFilter getPredictionUrl() {
        return predictionUrl;
    }

    public void setPredictionUrl(StringFilter predictionUrl) {
        this.predictionUrl = predictionUrl;
    }

    public StringFilter getProject() {
        return project;
    }

    public void setProject(StringFilter project) {
        this.project = project;
    }

    public StringFilter getVersion() {
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public IntegerFilter getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(IntegerFilter replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    @Override
    public String toString() {
        return "ModelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (algorithm != null ? "algorithm=" + algorithm + ", " : "") +
                (library != null ? "library=" + library + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (owner != null ? "owner=" + owner + ", " : "") +
                (modelLocation != null ? "modelLocation=" + modelLocation + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (deployedDate != null ? "deployedDate=" + deployedDate + ", " : "") +
                (trainingDataset != null ? "trainingDataset=" + trainingDataset + ", " : "") +
                (performanceMetrics != null ? "performanceMetrics=" + performanceMetrics + ", " : "") +
                (featureSignificance != null ? "featureSignificance=" + featureSignificance + ", " : "") +
                (builderConfig != null ? "builderConfig=" + builderConfig + ", " : "") +
                (predictionUrl != null ? "predictionUrl=" + predictionUrl + ", " : "") +
                (project != null ? "project=" + project + ", " : "") +
                (version != null ? "version=" + version + ", " : "") +
                (replicationFactor != null ? "replicationFactor=" + replicationFactor + ", " : "") +
            "}";
    }

}
