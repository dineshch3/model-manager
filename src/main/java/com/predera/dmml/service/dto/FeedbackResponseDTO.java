package com.predera.dmml.service.dto;

public class FeedbackResponseDTO {
	
	private String feedbackClass;
	private Float feedbackValue;
	
	public String getFeedbackClass() {
		return feedbackClass;
	}
	public void setFeedbackClass(String feedbackClass) {
		this.feedbackClass = feedbackClass;
	}
	public Float getFeedbackValue() {
		return feedbackValue;
	}
	public void setFeedbackValue(Float feedbackValue) {
		this.feedbackValue = feedbackValue;
	}
}
