package com.shenlandt.wh.pojo;

public class SearchFeatures {
	private String type;
	private SearchGeometry geometry;
	private SearchProperties properties;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SearchGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(SearchGeometry geometry) {
		this.geometry = geometry;
	}

	public SearchProperties getProperties() {
		return properties;
	}

	public void setProperties(SearchProperties properties) {
		this.properties = properties;
	}

}
