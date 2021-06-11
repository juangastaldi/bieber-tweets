package org.interview.service.impl;

import java.util.List;

import org.interview.dto.StatisticsResp;
import org.interview.entity.Statistics;
import org.interview.repository.StatisticsRepository;
import org.interview.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Override
	public StatisticsResp getStatistics() {
		List<Statistics> statistics = statisticsRepository.findAll();
		if (CollectionUtils.isEmpty(statistics)) {
			return new StatisticsResp();
		}
		Double avg = statistics.stream().mapToDouble(s -> new Double(s.getCount()) / s.getSeconds()).sum()
				/ statistics.size();
		return new StatisticsResp(new Long(statistics.size()), avg);
	}
}
