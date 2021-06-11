package org.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InterviewValidationException extends Exception {

	public InterviewValidationException(String message) {
		super(message);
	}

}
