package org.interview.service;

import org.interview.exception.InterviewValidationException;
import org.interview.exception.TwitterAuthenticationException;

public interface AuthenticatorService {

	public String getAuthorizeUrl() throws TwitterAuthenticationException;

	public void createAuthenticator(String providedPin, final Boolean overwriteInd)
			throws TwitterAuthenticationException, InterviewValidationException;

	public Boolean validateAuthenticator();
}
