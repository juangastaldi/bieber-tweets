package org.interview.dto;

public class ValidateAuthenticatorResp {

	private Boolean validAuthenticator;

	public ValidateAuthenticatorResp(Boolean validAuthenticator) {
		this.validAuthenticator = validAuthenticator;
	}

	public Boolean getValidAuthenticator() {
		return validAuthenticator;
	}

	public void setValidAuthenticator(Boolean validAuthenticator) {
		this.validAuthenticator = validAuthenticator;
	}

}
