package org.interview.oauth.twitter;

import java.io.IOException;

import org.interview.context.AppContext;
import org.interview.entity.Authenticator;
import org.interview.exception.TwitterAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

@Component
public class TwitterAuthenticatorHelper {

	@Autowired
	private AppContext appContext;

	private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	public OAuthAuthorizeTemporaryTokenUrl getAuthorizationURL(final Authenticator authenticator)
			throws TwitterAuthenticationException {
		OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = authenticator.getConsumerSecret();

		OAuthCredentialsResponse requestTokenResponse = getTemporaryToken(signer, authenticator.getConsumerKey());
		signer.tokenSharedSecret = requestTokenResponse.tokenSecret;

		OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(appContext.getAutorizeUrl());
		authorizeUrl.temporaryToken = requestTokenResponse.token;

		return authorizeUrl;
	}

	public HttpRequestFactory getRequestFactory(final Authenticator authenticator) {
		OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = authenticator.getConsumerSecret();
		signer.tokenSharedSecret = authenticator.getTokenSecret();

		OAuthParameters parameters = new OAuthParameters();
		parameters.consumerKey = authenticator.getConsumerKey();
		parameters.token = authenticator.getToken();
		parameters.signer = signer;

		return HTTP_TRANSPORT.createRequestFactory(parameters);
	}

	/**
	 * Retrieve the initial temporary tokens required to obtain the acces token
	 *
	 * @param signer The HMAC signer used to cryptographically sign requests to
	 *               Twitter
	 * @return The response containing the temporary tokens
	 */
	private OAuthCredentialsResponse getTemporaryToken(final OAuthHmacSigner signer, final String consumerKey)
			throws TwitterAuthenticationException {
		OAuthGetTemporaryToken requestToken = new OAuthGetTemporaryToken(appContext.getRequestTokenUrl());
		requestToken.consumerKey = consumerKey;
		requestToken.transport = HTTP_TRANSPORT;
		;
		requestToken.signer = signer;

		OAuthCredentialsResponse requestTokenResponse;
		try {
			requestTokenResponse = requestToken.execute();
		} catch (IOException e) {
			throw new TwitterAuthenticationException("Unable to aquire temporary token: " + e.getMessage(), e);
		}

		return requestTokenResponse;
	}

	public OAuthCredentialsResponse retrieveAccessTokens(final Authenticator authenticator, final String providedPin)
			throws TwitterAuthenticationException {
		OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = authenticator.getConsumerSecret();
		signer.tokenSharedSecret = authenticator.getToken();

		OAuthGetAccessToken accessToken = new OAuthGetAccessToken(appContext.getAccessTokenUrl());

		accessToken.verifier = providedPin;
		accessToken.consumerKey = authenticator.getConsumerSecret();
		accessToken.signer = signer;
		accessToken.transport = HTTP_TRANSPORT;
		accessToken.temporaryToken = authenticator.getToken();

		OAuthCredentialsResponse accessTokenResponse;
		try {
			accessTokenResponse = accessToken.execute();
		} catch (IOException e) {
			throw new TwitterAuthenticationException("Unable to authorize access: " + e.getMessage(), e);
		}

		return accessTokenResponse;
	}
}
