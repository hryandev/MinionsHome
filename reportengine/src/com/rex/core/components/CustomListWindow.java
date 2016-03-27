package com.rex.core.components;

import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CustomListWindow<T> extends Window implements Button.ClickListener{
	private static final long serialVersionUID = -1946950823954277509L;
	
	protected CustomTable cTable;
	protected Grid cList;
	
	protected JPAContainer<T> entity;
	protected IndexedContainer orgContainer;
    protected Button saveButton;
    protected Button cancelButton;
    
    public CustomListWindow(CustomTable cTable) {
    	/*entity = JPAContainerFactory.make(typeParameterClass,
    			ReportEngineUI.PERSISTENCE_UNIT);*/
    	
    	this.cTable = cTable;
    	orgContainer = this.cTable.getRevisedContainer();
    	cList = new Grid();
        buildLayout();
    }
    
    public void buildLayout(){
    	VerticalLayout root = new VerticalLayout();
    	
    	HorizontalLayout toolbar = new HorizontalLayout();
    	saveButton = new Button("Add", this);
        cancelButton = new Button("Cancel", this);
        toolbar.addComponent(saveButton);
        toolbar.addComponent(cancelButton);
        toolbar.setSpacing(true);
    	
        root.addComponents(toolbar, cList);
        root.setExpandRatio(cList, 1);
        root.setSpacing(true);
        
        setContent(root);
        //setCaption("Select Frequency");
    }

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
	}

}
