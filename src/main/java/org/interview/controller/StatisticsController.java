package org.interview.controller;

import org.interview.dto.StatisticsResp;
import org.interview.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Statistics")
@RestController
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	@GetMapping("statistics")
	public StatisticsResp getStatistics() {
		return statisticsService.getStatistics();
	}
}
