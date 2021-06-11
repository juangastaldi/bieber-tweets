package org.interview.controller;

import java.util.Objects;

import org.interview.dto.AuthorizeUrlResp;
import org.interview.dto.CreateAuthenticatorRqst;
import org.interview.dto.ValidateAuthenticatorResp;
import org.interview.exception.InterviewException;
import org.interview.exception.InterviewValidationException;
import org.interview.exception.TwitterAuthenticationException;
import org.interview.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Authenticator")
@RestController
@RequestMapping("/auth")
public class AuthenticatorController {

	@Autowired
	private AuthenticatorService authenticatorService;

	@GetMapping("authorize-url")
	public AuthorizeUrlResp getAuthorizeUrl() throws TwitterAuthenticationException {
		String url = authenticatorService.getAuthorizeUrl();
		return new AuthorizeUrlResp(url);
	}

	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("createAuthenticator")
	public void createAuthenticator(@RequestBody CreateAuthenticatorRqst request)
			throws TwitterAuthenticationException, InterviewException, InterviewValidationException {
		if (Objects.isNull(request) || Objects.isNull(request.getProvidedPin())) {
			throw new InterviewValidationException("ProvidedPin is required");
		}
		authenticatorService.createAuthenticator(request.getProvidedPin(), request.getOverwriteInd());
	}

	@GetMapping("validateAuthenticator")
	public ValidateAuthenticatorResp validateAuthenticator() {
		Boolean isValid = authenticatorService.validateAuthenticator();
		return new ValidateAuthenticatorResp(isValid);
	}
}
