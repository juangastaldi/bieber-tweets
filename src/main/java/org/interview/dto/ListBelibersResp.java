package org.interview.dto;

import java.util.List;

public class ListBelibersResp {

	private List<Belieber> belibers;

	public ListBelibersResp(List<Belieber> belibers) {

		this.belibers = belibers;
	}

	public List<Belieber> getBelibers() {
		return belibers;
	}

	public void setBelibers(List<Belieber> belibers) {
		this.belibers = belibers;
	}
}
