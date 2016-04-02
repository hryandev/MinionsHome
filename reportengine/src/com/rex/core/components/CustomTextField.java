package com.rex.core.components;

import com.vaadin.ui.TextField;

public class CustomTextField extends TextField{
	private boolean isThisRequired = false;

	public CustomTextField() {
		super();
	}

	public CustomTextField(Boolean isRequired) {
		this();
		if (isRequired != null && isRequired) {
			setRequired();
		}
	}

	public CustomTextField(Boolean isRequired, Integer length) {
		this();
		if (isRequired != null && isRequired) {
			setRequired();
		}
		setMaxLength(length);
	}

	public void properties() {
		properties(null);
	}

	public void properties(Boolean isRequired) {
		properties(isRequired, null);
	}

	public void properties(Boolean isRequired, Integer length) {
		properties(isRequired, length, null);
	}

	public void properties(Boolean isRequired, Integer length, String width) {
		properties(isRequired, length, null, null);
	}

	public void properties(Boolean isRequired, Integer length, String width,
			Boolean styleCase) {
		if (isRequired != null && isRequired) {
			setRequired();
		}
		if (length != null) {
			setMaxLength(length);
		}
		if (width != null) {
			setWidth(width);
		}
	}

	public void setRequired() {
		isThisRequired = true;
	}

	public void setError() {
		isThisRequired = false;
	}

	public void setDefault() {
		isThisRequired = false;
	}

	public CustomTextField(String caption, String value) {
		super(caption, value);
	}

	public boolean isThisRequired() {
		return isThisRequired;
	}

	public void setThisRequired(boolean isThisRequired) {
		this.isThisRequired = isThisRequired;
	}
}
