package com.predera.dmml.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Model.
 */
@Entity
@Table(name = "model")
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "algorithm", nullable = false)
    private String algorithm;

    @NotNull
    @Column(name = "jhi_library", nullable = false)
    private String library;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "model_location")
    private String modelLocation;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Column(name = "deployed_date", nullable = false)
    private Instant deployedDate;

    @NotNull
    @Lob
    @Column(name = "training_dataset", nullable = false)
    private String trainingDataset;

    @NotNull
    @Lob
    @Column(name = "performance_metrics", nullable = false)
    private String performanceMetrics;

    @NotNull
    @Lob
    @Column(name = "feature_significance", nullable = false)
    private String featureSignificance;

    @NotNull
    @Lob
    @Column(name = "builder_config", nullable = false)
    private String builderConfig;

    @Column(name = "prediction_url")
    private String predictionUrl;

    @NotNull
    @Column(name = "project", nullable = false)
    private String project;

    @NotNull
    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "replication_factor")
    private Integer replicationFactor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Model name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Model type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Model algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getLibrary() {
        return library;
    }

    public Model library(String library) {
        this.library = library;
        return this;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getStatus() {
        return status;
    }

    public Model status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public Model owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModelLocation() {
        return modelLocation;
    }

    public Model modelLocation(String modelLocation) {
        this.modelLocation = modelLocation;
        return this;
    }

    public void setModelLocation(String modelLocation) {
        this.modelLocation = modelLocation;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Model createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getDeployedDate() {
        return deployedDate;
    }

    public Model deployedDate(Instant deployedDate) {
        this.deployedDate = deployedDate;
        return this;
    }

    public void setDeployedDate(Instant deployedDate) {
        this.deployedDate = deployedDate;
    }

    public String getTrainingDataset() {
        return trainingDataset;
    }

    public Model trainingDataset(String trainingDataset) {
        this.trainingDataset = trainingDataset;
        return this;
    }

    public void setTrainingDataset(String trainingDataset) {
        this.trainingDataset = trainingDataset;
    }

    public String getPerformanceMetrics() {
        return performanceMetrics;
    }

    public Model performanceMetrics(String performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
        return this;
    }

    public void setPerformanceMetrics(String performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public String getFeatureSignificance() {
        return featureSignificance;
    }

    public Model featureSignificance(String featureSignificance) {
        this.featureSignificance = featureSignificance;
        return this;
    }

    public void setFeatureSignificance(String featureSignificance) {
        this.featureSignificance = featureSignificance;
    }

    public String getBuilderConfig() {
        return builderConfig;
    }

    public Model builderConfig(String builderConfig) {
        this.builderConfig = builderConfig;
        return this;
    }

    public void setBuilderConfig(String builderConfig) {
        this.builderConfig = builderConfig;
    }

    public String getPredictionUrl() {
        return predictionUrl;
    }

    public Model predictionUrl(String predictionUrl) {
        this.predictionUrl = predictionUrl;
        return this;
    }

    public void setPredictionUrl(String predictionUrl) {
        this.predictionUrl = predictionUrl;
    }

    public String getProject() {
        return project;
    }

    public Model project(String project) {
        this.project = project;
        return this;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getVersion() {
        return version;
    }

    public Model version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getReplicationFactor() {
        return replicationFactor;
    }

    public Model replicationFactor(Integer replicationFactor) {
        this.replicationFactor = replicationFactor;
        return this;
    }

    public void setReplicationFactor(Integer replicationFactor) {
        this.replicationFactor = replicationFactor;
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
        Model model = (Model) o;
        if (model.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), model.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Model{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", algorithm='" + getAlgorithm() + "'" +
            ", library='" + getLibrary() + "'" +
            ", status='" + getStatus() + "'" +
            ", owner='" + getOwner() + "'" +
            ", modelLocation='" + getModelLocation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", deployedDate='" + getDeployedDate() + "'" +
            ", trainingDataset='" + getTrainingDataset() + "'" +
            ", performanceMetrics='" + getPerformanceMetrics() + "'" +
            ", featureSignificance='" + getFeatureSignificance() + "'" +
            ", builderConfig='" + getBuilderConfig() + "'" +
            ", predictionUrl='" + getPredictionUrl() + "'" +
            ", project='" + getProject() + "'" +
            ", version='" + getVersion() + "'" +
            ", replicationFactor=" + getReplicationFactor() +
            "}";
    }
}
