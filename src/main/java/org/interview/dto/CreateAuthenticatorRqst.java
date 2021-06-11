package org.interview.dto;

public class CreateAuthenticatorRqst {

	private String providedPin;
	private Boolean overwriteInd;

	public String getProvidedPin() {
		return providedPin;
	}

	public void setProvidedPin(String providedPin) {
		this.providedPin = providedPin;
	}

	public Boolean getOverwriteInd() {
		return overwriteInd;
	}

	public void setOverwriteInd(Boolean overwriteInd) {
		this.overwriteInd = overwriteInd;
	}
}
