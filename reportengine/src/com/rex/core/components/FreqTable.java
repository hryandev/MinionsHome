package com.rex.core.components;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class FreqTable extends VerticalLayout{
	
	Grid freqList;
	
	IndexedContainer container = new IndexedContainer();
	
	/*public ValueChangeListener update = new ValueChangeListener() {

		@Override
		public void valueChange(ValueChangeEvent event) {
			// TODO Auto-generated method stub
			if (freqList == null) {
				freqList = new Grid("Frequency");
				freqList.setContainerDataSource(container);
                addComponent(freqList);
            }
		}
	};*/
	
	public FreqTable(){
		freqList = new Grid("Frequency");
		
		configure();
		buildLayout();
	}
	
	public void configure(){
		
        
		freqList.setSelectionMode(SelectionMode.MULTI);
		
		freqList.setImmediate(true);
		
		for (Column c : freqList.getColumns()) {
            //if (!c.getPropertyId().equals("icon")) {
                c.setSortable(true);
            //}
            c.setHidable(true);
        }
		
		freqList.setColumnReorderingAllowed(true);
		freqList.setHeightByRows(6);
		
	}
	
	public void buildLayout(){
		HorizontalLayout toolbar = new HorizontalLayout();
        Button newButton = new Button("Add");
        Button delButton = new Button("Delete");
        toolbar.addComponent(newButton);
        toolbar.addComponent(delButton);
        
        //toolbar.setWidth("100px");
        //toolbar.setHeight("50px");
        
        //freqList.setWidth("200px");
        //freqList.setHeight("500px");
        
        addComponent(toolbar);
        addComponent(freqList);
        
        
        //setExpandRatio(toolbar, 1);
        
        //setComponentAlignment(freqList, Alignment.BOTTOM_LEFT);
        
        newButton.addClickListener(e -> openFreqsWindow());
        
        setSizeFull();
		
		//setMargin(true);
		setSpacing(true);
	}
	
	public Grid getFreqList(){
		return freqList;
	}
	
	public IndexedContainer getOrgContainer(){
		return container;
	}
		
	public void openFreqsWindow(){
    	FreqsListWindow flw = new FreqsListWindow(this);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
        
        /*FreqsListWindow flw = new FreqsListWindow(job, freqTable);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();*/
        
    }	
	
	public void update(IndexedContainer container){
		this.container = container;
		
		removeComponent(freqList);
		freqList = new Grid("Frequency");
		freqList.setContainerDataSource(container);
		
		freqList.removeColumn("id");
		addComponent(freqList);
        
	}

}
