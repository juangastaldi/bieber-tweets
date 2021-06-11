package org.interview.dto;

import java.util.List;

public class Belieber implements Comparable<Belieber> {

	private User user;
	private List<Tweet> tweets;

	public Belieber(User user, List<Tweet> tweets) {
		this.user = user;
		this.tweets = tweets;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	@Override
	public int compareTo(Belieber resp) {
		return this.user.compareTo(resp.user);
	}
}
