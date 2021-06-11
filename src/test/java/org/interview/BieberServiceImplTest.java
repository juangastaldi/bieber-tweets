package org.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.interview.context.AppContext;
import org.interview.dto.Belieber;
import org.interview.dto.Tweet;
import org.interview.exception.InterviewException;
import org.interview.exception.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.interview.repository.StatisticsRepository;
import org.interview.service.impl.BieberServiceImpl;
import org.interview.util.ConnectionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import com.google.common.collect.Lists;

@SpringBootTest
public class BieberServiceImplTest {

	@Mock
	private ConnectionUtil connectionUtil;
	@Mock
	private StatisticsRepository statisticsRepository;
	@Mock
	private TwitterAuthenticator twitterAuthenticator;
	@Mock
	private AppContext appContext;

	@InjectMocks
	private BieberServiceImpl bieberService;

	@Test
	public void oneRow() throws TwitterAuthenticationException, InterviewException {
		List<String> lines = Lists.newArrayList("{\"created_at\":\"Sat May 29 14:49:03 +0000 2021\"," + "\"id_srt\":9,"
				+ "\"text\":\"test\"," + "\"user\":{\"id_str\":\"1\"," + "\"name\":\"ale\","
				+ "\"created_at\":\"Thu Mar 11 23:50:49 +0000 2021\"" + "}}");

		when(connectionUtil.readStream(any(), any(), any())).thenReturn(Pair.of(lines, 1L));

		List<Belieber> resp = bieberService.listBeliebers();
		assertEquals(1, resp.size());
		verify(statisticsRepository, times(1)).save(any());
	}

	@Test
	public void noResult() throws InterviewException, TwitterAuthenticationException {

		Mockito.when(connectionUtil.readStream(any(), any(), any())).thenReturn(Pair.of(Lists.newArrayList(), 0L));

		List<Belieber> resp = bieberService.listBeliebers();
		assertEquals(0, resp.size());
		verify(statisticsRepository, times(1)).save(any());
	}

	@Test
	public void tweetOrder() throws TwitterAuthenticationException, InterviewException {
		List<String> lines = Lists.newArrayList(
				"{\"created_at\":\"Sat May 29 14:49:03 +0000 2021\"," + "\"id_str\":1," + "\"text\":\"test\","
						+ "\"user\":{\"id_str\":\"1\"," + "\"name\":\"ale\","
						+ "\"created_at\":\"Thu Mar 11 23:50:49 +0000 2021\"" + "}}",
				"{\"created_at\":\"Sat May 29 14:49:02 +0000 2021\"," + "\"id_str\":2," + "\"text\":\"test\","
						+ "\"user\":{\"id_str\":\"1\"," + "\"name\":\"ale\","
						+ "\"created_at\":\"Thu Mar 11 23:50:49 +0000 2021\"" + "}}",
				"{\"created_at\":\"Sat May 29 14:49:04 +0000 2021\"," + "\"id_str\":3," + "\"text\":\"test\","
						+ "\"user\":{\"id_str\":\"1\"," + "\"name\":\"ale\","
						+ "\"created_at\":\"Thu Mar 11 23:50:49 +0000 2021\"" + "}}");

		Mockito.when(connectionUtil.readStream(any(), any(), any())).thenReturn(Pair.of(lines, 3L));

		List<Belieber> resp = bieberService.listBeliebers();
		List<Tweet> tweets = resp.get(0).getTweets();
		assertEquals("2", tweets.get(0).getId());
		assertEquals("1", tweets.get(1).getId());
		assertEquals("3", tweets.get(2).getId());
		verify(statisticsRepository, times(1)).save(any());
	}

	@Test
	public void userOrder() throws TwitterAuthenticationException, InterviewException {
		List<String> lines = Lists.newArrayList(
				"{\"created_at\":\"Sat May 29 14:49:03 +0000 2021\"," + "\"id_str\":1," + "\"text\":\"test\","
						+ "\"user\":{\"id_str\":\"1\"," + "\"name\":\"ale\","
						+ "\"created_at\":\"Thu Mar 11 23:50:49 +0000 2021\"" + "}}",
				"{\"created_at\":\"Sat May 29 14:49:02 +0000 2020\"," + "\"id_str\":2," + "\"text\":\"test\","
						+ "\"user\":{\"id_str\":\"2\"," + "\"name\":\"ale\","
						+ "\"created_at\":\"Thu Mar 11 23:50:49 +0000 2020\"" + "}}",
				"{\"created_at\":\"Sat May 29 14:49:04 +0000 2021\"," + "\"id_str\":3," + "\"text\":\"test\","
						+ "\"user\":{\"id_str\":\"3\"," + "\"name\":\"ale\","
						+ "\"created_at\":\"Thu Mar 15 23:50:49 +0000 2021\"" + "}}");

		Mockito.when(connectionUtil.readStream(any(), any(), any())).thenReturn(Pair.of(lines, 3L));

		List<Belieber> resp = bieberService.listBeliebers();
		assertEquals("2", resp.get(0).getUser().getId());
		assertEquals("1", resp.get(1).getUser().getId());
		assertEquals("3", resp.get(2).getUser().getId());
		verify(statisticsRepository, times(1)).save(any());
	}
}
