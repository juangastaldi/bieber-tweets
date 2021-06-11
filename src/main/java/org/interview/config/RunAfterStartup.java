package org.interview.config;

import java.util.Optional;

import org.interview.entity.Authenticator;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.interview.oauth.twitter.TwitterAuthenticatorHelper;
import org.interview.repository.AuthenticatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RunAfterStartup {

	@Autowired
	private AuthenticatorRepository authenticatorRepository;

	@Autowired
	private TwitterAuthenticator twitterAuthenticator;

	@Autowired
	private TwitterAuthenticatorHelper twitterAuthenticatorHelper;

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
		Optional<Authenticator> savedAuthenticator = authenticatorRepository.findByValid(true);
		if (savedAuthenticator.isPresent()) {
			twitterAuthenticator.setAuthenticator(savedAuthenticator.get());
			twitterAuthenticator.setFactory(twitterAuthenticatorHelper.getRequestFactory(savedAuthenticator.get()));
		}
	}
}
