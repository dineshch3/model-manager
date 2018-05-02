package com.predera.dmml.service.dto;

import java.time.Instant;

public interface PredictionSubset {
	Long getId();
	Instant getTimestamp();
	Long getQueryId();
	Float getOutputValue();
	Float getFeedbackValue();
	Boolean getFeedbackFlag();
	Instant getFeedbackTimestamp();
	String getFeedbackUser();
}
