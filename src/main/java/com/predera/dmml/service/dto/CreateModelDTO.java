package com.predera.dmml.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateModelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private String name;

	@NotNull
	private String type;

	@NotNull
	private String algorithm;
	
	@NotNull
	private String project;
	
	@NotNull
	private String version;

	@NotNull
	private String performanceMetrics;

	@NotNull
	private String featureSignificance;

	@NotNull
	private String builderConfig;

	@NotNull
	private String trainingDataset;
	
	@NotNull
	private String library;
	
	private String modelLocation;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getPerformanceMetrics() {
		return performanceMetrics;
	}

	public void setPerformanceMetrics(String performanceMetrics) {
		this.performanceMetrics = performanceMetrics;
	}

	public String getFeatureSignificance() {
		return featureSignificance;
	}

	public void setFeatureSignificance(String featureSignificance) {
		this.featureSignificance = featureSignificance;
	}

	public String getBuilderConfig() {
		return builderConfig;
	}

	public void setBuilderConfig(String builderConfig) {
		this.builderConfig = builderConfig;
	}

	public String getTrainingDataset() {
		return trainingDataset;
	}

	public void setTrainingDataset(String trainingDataset) {
		this.trainingDataset = trainingDataset;
	}

	public String getModelLocation() {
		return modelLocation;
	}

	public void setModelLocation(String modelLocation) {
		this.modelLocation = modelLocation;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Model{" +

				", name='" + getName() + "'" + ", type='" + getType() + "'" + ", algorithm='" + getAlgorithm() + "'" +
				", performanceMetrics='" + getPerformanceMetrics() + "'" +
				", featureSignificance='" + getFeatureSignificance() + "'" + ", builderConfig='" + getBuilderConfig()
				+ "'" + ", trainingDataSet='" + getTrainingDataset() + "'" +
				"}";
	}
}
