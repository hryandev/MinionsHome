package com.rex.core.components;

public class AutoSuggestResultBean {

	private String id;
	private String value;

	public AutoSuggestResultBean() {
		super();
	}

	public AutoSuggestResultBean(String id, String value) {
		this();
		this.id = id;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
