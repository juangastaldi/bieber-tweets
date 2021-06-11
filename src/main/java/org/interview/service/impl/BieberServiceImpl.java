package org.interview.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.interview.context.AppContext;
import org.interview.dto.Belieber;
import org.interview.dto.Tweet;
import org.interview.dto.User;
import org.interview.entity.Statistics;
import org.interview.exception.InterviewException;
import org.interview.exception.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.interview.repository.StatisticsRepository;
import org.interview.service.BieberService;
import org.interview.util.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpRequestFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class BieberServiceImpl implements BieberService {

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private ConnectionUtil connectionUtil;

	@Autowired
	private AppContext appContext;

	@Autowired
	private TwitterAuthenticator twitterAuthenticator;

	/**
	 * Search all the tweets that have the word "bieber". Use a
	 * {@link TwitterAuthenticator} to access to Twiter API
	 * 
	 * @param twitterAuthenticator
	 * @return List of BelieerResp
	 * @throws InterviewException
	 * @throws TwitterAuthenticationException
	 */
	public List<Belieber> listBeliebers() throws InterviewException, TwitterAuthenticationException {

		HttpRequestFactory httpFactory = twitterAuthenticator.getFactory();

		Map<String, String> parameters = Maps.newHashMap();
		parameters.put("track", "bieber");
		Pair<List<String>, Long> lines = connectionUtil.readStream(httpFactory, appContext.getTweeterFilterPath(),
				parameters);

		List<Tweet> tweets = parseTweets(lines.getFirst());
		createStatistics(lines);
		return getBelieverResp(tweets);
	}

	private void createStatistics(Pair<List<String>, Long> lines) {
		Statistics statistics = new Statistics();
		statistics.setDate(new Date());
		statistics.setCount(new Long(lines.getFirst().size()));
		statistics.setSeconds(lines.getSecond() / 1000);
		statisticsRepository.save(statistics);
	}

	/**
	 * Create the list of response DTO
	 * 
	 * @param allTweets
	 * @return
	 */
	private List<Belieber> getBelieverResp(List<Tweet> allTweets) {
		List<Belieber> resp = Lists.newArrayList();

		Map<User, List<Tweet>> mTweets = allTweets.stream().collect(Collectors.groupingBy(Tweet::getUser));

		for (Entry<User, List<Tweet>> eTweets : mTweets.entrySet()) {
			User user = eTweets.getKey();
			List<Tweet> userTweets = eTweets.getValue();
			Collections.sort(userTweets);
			resp.add(new Belieber(user, userTweets));
		}

		Collections.sort(resp);
		return resp;
	}

	/**
	 * Parse the strings with the info of each tweet into {@link Tweet}
	 * 
	 * @param sTweets
	 * @return List of Tweet
	 */
	private List<Tweet> parseTweets(List<String> sTweets) {
		ObjectMapper mapper = new ObjectMapper();
		List<Tweet> tweets = Lists.newArrayList();

		for (String sTweet : sTweets) {
			try {
				tweets.add(mapper.readValue(sTweet, Tweet.class));
			} catch (JsonProcessingException e) {
				continue;
			}
		}
		return tweets;
	}

}
