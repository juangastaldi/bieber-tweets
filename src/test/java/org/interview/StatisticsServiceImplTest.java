package org.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.interview.dto.StatisticsResp;
import org.interview.entity.Statistics;
import org.interview.repository.StatisticsRepository;
import org.interview.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Lists;

@SpringBootTest
public class StatisticsServiceImplTest {

	@Mock
	private StatisticsRepository statisticsRepository;

	@InjectMocks
	private StatisticsServiceImpl statisticsService;

	@Test
	public void emptyList() {
		when(statisticsRepository.findAll()).thenReturn(Lists.newArrayList());
		StatisticsResp resp = statisticsService.getStatistics();
		assertNull(resp.getApiCalls());
		assertNull(resp.getMessagesPerSecondAverage());
	}

	@Test
	public void getStatistics() {
		Statistics s1 = new Statistics();
		s1.setCount(30L);
		s1.setSeconds(30L);
		Statistics s2 = new Statistics();
		s2.setCount(60L);
		s2.setSeconds(30L);
		when(statisticsRepository.findAll()).thenReturn(Lists.newArrayList(s1, s2));
		StatisticsResp resp = statisticsService.getStatistics();
		assertEquals(new Double(1.5), resp.getMessagesPerSecondAverage());
		assertEquals(2L, resp.getApiCalls());
	}
}
