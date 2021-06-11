package org.interview.dto;

public class StatisticsResp {

	private Long apiCalls;
	private Double messagesPerSecondAverage;

	public StatisticsResp(Long apiCalls, Double avg) {
		this.apiCalls = apiCalls;
		this.messagesPerSecondAverage = avg;
	}

	public StatisticsResp() {
	}

	public Long getApiCalls() {
		return apiCalls;
	}

	public void setApiCalls(Long apiCalls) {
		this.apiCalls = apiCalls;
	}

	public Double getMessagesPerSecondAverage() {
		return messagesPerSecondAverage;
	}

	public void setMessagesPerSecondAverage(Double messagesPerSecondAverage) {
		this.messagesPerSecondAverage = messagesPerSecondAverage;
	}

}
