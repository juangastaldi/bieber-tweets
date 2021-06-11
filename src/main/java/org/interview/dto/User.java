package org.interview.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Comparable<User> {

	@JsonProperty("id_str")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("screen_name")
	private String screenName;
	@JsonProperty("created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM d HH:mm:ss Z yyyy")
	private Date creationDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public int compareTo(User user) {
		return this.creationDate.compareTo(user.creationDate);
	}

	@Override
	public boolean equals(Object user) {
		return this.id.equals(((User) user).id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
