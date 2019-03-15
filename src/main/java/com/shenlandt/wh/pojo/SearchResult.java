package com.shenlandt.wh.pojo;

import java.util.List;

public class SearchResult {
	private String type;
	private List<SearchFeatures> features;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SearchFeatures> getFeatures() {
		return features;
	}

	public void setFeatures(List<SearchFeatures> features) {
		this.features = features;
	}

}
