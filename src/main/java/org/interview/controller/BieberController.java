package org.interview.controller;

import java.util.List;

import org.interview.dto.Belieber;
import org.interview.dto.ListBelibersResp;
import org.interview.exception.InterviewException;
import org.interview.exception.TwitterAuthenticationException;
import org.interview.service.BieberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Belibers")
@RestController
public class BieberController {

	@Autowired
	private BieberService bieberService;

	@GetMapping("list-belibers")
	public ListBelibersResp listBelibers() throws InterviewException, TwitterAuthenticationException {

		List<Belieber> belibers = bieberService.listBeliebers();
		return new ListBelibersResp(belibers);
	}
}
