package org.interview.oauth.twitter;

import org.interview.entity.Authenticator;

import com.google.api.client.http.HttpRequestFactory;

public class TwitterAuthenticator {

	private HttpRequestFactory factory;
	private Authenticator authenticator;

	public TwitterAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public HttpRequestFactory getFactory() {
		return factory;
	}

	public void setFactory(HttpRequestFactory factory) {
		this.factory = factory;
	}

	public Authenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

}
