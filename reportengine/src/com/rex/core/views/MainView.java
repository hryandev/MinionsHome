package com.rex.core.views;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;


/**
 * 
 * @author Ryan Hsu
 */

@Theme("valo")
@Title("")
public class MainView extends HorizontalLayout implements View{
	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		Notification.show("Welcome to Report EngineXcel");
	}

}
