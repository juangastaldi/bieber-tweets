package org.interview;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.interview.context.AppContext;
import org.interview.entity.Authenticator;
import org.interview.exception.InterviewValidationException;
import org.interview.exception.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.interview.oauth.twitter.TwitterAuthenticatorHelper;
import org.interview.repository.AuthenticatorRepository;
import org.interview.service.impl.AuthenticatorServiceImpl;
import org.interview.util.ConnectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

@SpringBootTest
public class AuthenticatorServiceImplTest {

	@Mock
	private TwitterAuthenticator twitterAuthenticator;
	@Mock
	private AuthenticatorRepository authenticatorRepository;
	@Mock
	private ConnectionUtil connectionUtil;
	@Mock
	private AppContext appContext;
	@Mock
	private TwitterAuthenticatorHelper twitterAuthenticatorHelper;
	@InjectMocks
	private AuthenticatorServiceImpl authenticatorService;

	@BeforeEach
	public void setUp() throws TwitterAuthenticationException {
		OAuthCredentialsResponse oAuthCredentialsResponse = new OAuthCredentialsResponse();
		when(twitterAuthenticatorHelper.retrieveAccessTokens(any(), any())).thenReturn(oAuthCredentialsResponse);
		when(twitterAuthenticatorHelper.getRequestFactory(any()))
				.thenReturn(new NetHttpTransport().createRequestFactory());
		Authenticator auth = new Authenticator();
		when(twitterAuthenticator.getAuthenticator()).thenReturn(auth);
	}

	@Test
	public void createNewAuthenticator() throws TwitterAuthenticationException, InterviewValidationException {

		when(authenticatorRepository.findByValid(any())).thenReturn(Optional.empty());

		authenticatorService.createAuthenticator("123", false);

		verify(authenticatorRepository, times(1)).save(any());
	}

	@Test
	public void createAuthenticatorOverWrite() throws TwitterAuthenticationException, InterviewValidationException {

		when(authenticatorRepository.findByValid(any())).thenReturn(Optional.of(new Authenticator()));

		authenticatorService.createAuthenticator("123", true);

		verify(authenticatorRepository, times(2)).save(any());
	}

	@Test
	public void createAuthenticatorErrorOverWrite()
			throws TwitterAuthenticationException, InterviewValidationException {
		when(authenticatorRepository.findByValid(any())).thenReturn(Optional.of(new Authenticator()));

		assertThrows(InterviewValidationException.class, () -> authenticatorService.createAuthenticator("123", null));

		verify(authenticatorRepository, times(0)).save(any());
	}
}
