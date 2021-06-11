package org.interview.service;

import java.util.List;

import org.interview.dto.Belieber;
import org.interview.exception.InterviewException;
import org.interview.exception.TwitterAuthenticationException;

public interface BieberService {

	public List<Belieber> listBeliebers() throws InterviewException, TwitterAuthenticationException;
}
