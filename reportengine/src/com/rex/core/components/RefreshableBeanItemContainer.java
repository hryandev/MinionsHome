package com.rex.core.components;

import java.util.Collection;

import com.vaadin.data.util.BeanItemContainer;

public class RefreshableBeanItemContainer<FreqTest> extends BeanItemContainer<FreqTest> {
	private static final long serialVersionUID = 5226171149436137999L;

	public RefreshableBeanItemContainer(Collection<? extends FreqTest> collection) throws IllegalArgumentException {
		super(collection);
	}

	public RefreshableBeanItemContainer(Class<? super FreqTest> type) throws IllegalArgumentException {
		super(type);
	}

	public RefreshableBeanItemContainer(Class<? super FreqTest> type, Collection<? extends FreqTest> collection)
			throws IllegalArgumentException {
		super(type, collection);
	}

	public void refreshItems() {
		fireItemSetChange();
	}
}
