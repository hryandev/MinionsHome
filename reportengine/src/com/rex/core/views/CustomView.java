package com.rex.core.views;

import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

public class CustomView<T> extends HorizontalLayout implements View{
	private static final long serialVersionUID = 4264137215094677781L;
	
    public Grid gridList = new Grid();

    private JPAContainer<T> entity;
    protected Class<T> typeParameterClass;
    
    public CustomView(){
		entity = JPAContainerFactory.make(typeParameterClass,
	               ReportEngineUI.PERSISTENCE_UNIT);
		
		gridList.setContainerDataSource(entity);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
