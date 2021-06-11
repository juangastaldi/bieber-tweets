package org.interview.service.impl;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.interview.context.AppContext;
import org.interview.entity.Authenticator;
import org.interview.exception.InterviewValidationException;
import org.interview.exception.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.interview.oauth.twitter.TwitterAuthenticatorHelper;
import org.interview.repository.AuthenticatorRepository;
import org.interview.service.AuthenticatorService;
import org.interview.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

@Service
public class AuthenticatorServiceImpl implements AuthenticatorService {

	Logger logger = LoggerFactory.getLogger(AuthenticatorServiceImpl.class);

	@Autowired
	private TwitterAuthenticator twitterAuthenticator;

	@Autowired
	private AuthenticatorRepository authenticatorRepository;

	@Autowired
	private ConnectionUtil connectionUtil;

	@Autowired
	private AppContext appContext;

	@Autowired
	private TwitterAuthenticatorHelper twitterAuthenticatorHelper;

	@Override
	public String getAuthorizeUrl() throws TwitterAuthenticationException {
		OAuthAuthorizeTemporaryTokenUrl tempTokenUrl = twitterAuthenticatorHelper
				.getAuthorizationURL(twitterAuthenticator.getAuthenticator());
		twitterAuthenticator.getAuthenticator().setToken(tempTokenUrl.temporaryToken);
		return tempTokenUrl.build();
	}

	@Transactional
	@Override
	public void createAuthenticator(final String providedPin, final Boolean overwriteInd)
			throws TwitterAuthenticationException, InterviewValidationException {
		Optional<Authenticator> savedAuthenticator = authenticatorRepository.findByValid(true);

		if (savedAuthenticator.isPresent()) {
			if (Boolean.TRUE.equals(overwriteInd)) {
				Authenticator oldAuthenticator = savedAuthenticator.get();
				oldAuthenticator.setValid(false);
				authenticatorRepository.save(oldAuthenticator);
			} else {
				throw new InterviewValidationException(
						"Authenticator already created. To create a new Authenticator send overwriteInd");
			}
		}

		OAuthCredentialsResponse accessTokenResponse = twitterAuthenticatorHelper
				.retrieveAccessTokens(twitterAuthenticator.getAuthenticator(), providedPin);
		twitterAuthenticator.getAuthenticator().setToken(accessTokenResponse.token);
		twitterAuthenticator.getAuthenticator().setTokenSecret(accessTokenResponse.tokenSecret);

		twitterAuthenticator
				.setFactory(twitterAuthenticatorHelper.getRequestFactory(twitterAuthenticator.getAuthenticator()));
		twitterAuthenticator.getAuthenticator().setValid(true);
		authenticatorRepository.save(twitterAuthenticator.getAuthenticator());

	}

	@Override
	public Boolean validateAuthenticator() {
		HttpRequestFactory http = twitterAuthenticator.getFactory();

		if (Objects.isNull(http)) {
			return false;
		}
		Optional<Authenticator> savedAuth = authenticatorRepository.findByValid(true);

		try {
			HttpResponse response = connectionUtil.executeGetRequest(http, appContext.getVerifyCredentialsPath());

			if (response.getStatusCode() == 200) {
				return true;
			}
		} catch (IOException e) {
			logger.debug("Error validating authenticator ", e);
		} finally {
			if (savedAuth.isPresent()) {
				Authenticator authInvalid = savedAuth.get();
				authInvalid.setValid(false);
				authenticatorRepository.save(authInvalid);
			}
		}

		return false;
	}

}
