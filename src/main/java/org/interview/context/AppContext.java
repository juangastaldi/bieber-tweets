package org.interview.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppContext {

	@Value("${twitter.auth.consumerKey}")
	private String consumerKey;
	@Value("${twitter.auth.consumerSecret}")
	private String consumerSecret;
	@Value("${twitter.auth.autorizeUrl}")
	private String autorizeUrl;
	@Value("${twitter.auth.accessTokenUrl}")
	private String accessTokenUrl;
	@Value("${twitter.auth.requestTokenUrl}")
	private String requestTokenUrl;
	@Value("${twitter.verifyCredentials.path}")
	private String verifyCredentialsPath;
	@Value("${twitter.filter.path}")
	private String tweeterFilterPath;
	@Value("${twitter.filter.timeLimit}")
	private Long filterTimeLimit;
	@Value("${twitter.filter.rowLimit}")
	private Long filterRowLimit;

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public String getAutorizeUrl() {
		return autorizeUrl;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public String getRequestTokenUrl() {
		return requestTokenUrl;
	}

	public String getVerifyCredentialsPath() {
		return verifyCredentialsPath;
	}

	public String getTweeterFilterPath() {
		return tweeterFilterPath;
	}

	public Long getFilterTimeLimit() {
		return filterTimeLimit;
	}

	public Long getFilterRowLimit() {
		return filterRowLimit;
	}
}
