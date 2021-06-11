package org.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InterviewException extends Exception {

	public InterviewException(final String message, final Throwable t) {
		super(message, t);
	}

	public InterviewException(String message) {
		super(message);
	}
}
