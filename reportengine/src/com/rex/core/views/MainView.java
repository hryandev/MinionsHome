package com.rex.core.views;

import com.rex.components.valo.ValoMenuLayout;
import com.rex.core.ReportEngineUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;


/**
 * 
 * @author Ryan Hsu
 */

@Theme("valo")
@Title("Main")
public class MainView extends HorizontalLayout implements View{
	
	public static final String NAME = "main";

	public MainView(){
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//getUI().setContent(ReportEngineUI.root);
		//getUI().getNavigator().destroy();
		
		//ComponentContainer viewDisplay = root.getContentContainer();
		
		// TODO Auto-generated method stub
		String username = String.valueOf(getSession().getAttribute("user"));
		
		Notification.show("Hi, "+username+". Welcome to Report EngineXcel");
	}

}

