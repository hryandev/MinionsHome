package com.rex.core.components;

import com.rex.core.components.FreqsListWindow.EditorSavedEvent;
import com.rex.core.components.FreqsListWindow.EditorSavedListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class FreqTable extends VerticalLayout{
	
	Grid freqList;
	
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
		
		//freqList.setContainerDataSource(null);
        
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
		//freqList.setSizeUndefined();
	}
	
	public void buildLayout(){
		HorizontalLayout toolbar = new HorizontalLayout();
        Button newButton = new Button("Add");
        Button delButton = new Button("Delete");
        toolbar.addComponent(newButton);
        toolbar.addComponent(delButton);
        
        //addComponent(toolbar);
        addComponent(freqList);
        //setExpandRatio(freqList, 1);
        //newButton.addClickListener(e -> openFreqsWindow());
        
		
		setMargin(true);
		setSpacing(true);
	}
	
	public Grid getFreqList(){
		return freqList;
	}
		
	public void openFreqsWindow(){
    	FreqsListWindow flw = new FreqsListWindow();
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
        //setEnabled(false);
 
    	flw.addListener(new EditorSavedListener() {
            public void editorSaved(EditorSavedEvent event) {
            	//freqContainer.addAll(flw.getFreqList());
            	
				/*for (Iterator iterator = flw.getFreqList().iterator(); iterator.hasNext();) {
					Freq item = (Freq) iterator.next();
					
					freqContainer.addBean(item);
				}*/
            	
				//freqContainer.refreshItems();
				//freqTable.setEnabled(true);
				//freqList.setContainerDataSource(container);
            }
        });
    }	
	
	public FreqTable update(IndexedContainer container){
		removeComponent(freqList);
		freqList = new Grid("Frequency");
		freqList.setContainerDataSource(container);
		addComponent(freqList);
        
		return this;
	}

}
