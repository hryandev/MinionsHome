package com.rex.core.components;

import java.util.Collection;

import com.rex.backend.entity.Freq;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Ryan Hsu
 */

public class FreqTable extends VerticalLayout{
	private static final long serialVersionUID = 7250371074726485829L;

	Grid freqList;
	IndexedContainer revised_container = new IndexedContainer();
	public IndexedContainer org_container = new IndexedContainer();
	
	public FreqTable(){
		freqList = new Grid("Frequency");
		
		configure();
		buildLayout();
	}
	
	public void configure(){
		freqList.setSelectionMode(SelectionMode.MULTI);
		freqList.setImmediate(true);
		
		/*for (Column c : freqList.getColumns()) {
            if (!c.getPropertyId().equals("icon")) {
                c.setSortable(true);
            }
            c.setHidable(true);
        }*/
		
		freqList.setColumnReorderingAllowed(true);
		freqList.setHeightByRows(6);
		
	}
	
	public void buildLayout(){
		HorizontalLayout toolbar = new HorizontalLayout();
        Button newButton = new Button("Add");
        Button delButton = new Button("Delete");
        toolbar.addComponent(newButton);
        toolbar.addComponent(delButton);
        
        addComponent(toolbar);
        addComponent(freqList);
        
        //setExpandRatio(toolbar, 1);
        
        newButton.addClickListener(e -> openFreqsWindow());
        delButton.addClickListener(e -> removeSelectedFreq(freqList.getSelectedRows()));
        /*delButton.addClickListener(e -> {
    	    for (Object itemId: freqList.getSelectedRows()){
    	    	freqList.getContainerDataSource().removeItem(itemId);
    	    	revised_container.removeItem(itemId);
    	    }

    	    freqList.getSelectionModel().reset();
        });*/
        
        
        setSizeFull();
		
		//setMargin(true);
		setSpacing(true);
	}
	
	public Grid getFreqList(){
		return freqList;
	}
	
	public IndexedContainer getOrgContainer(){
		return org_container;
	}
	
	public void setOrgContainer(IndexedContainer container){
		org_container = container;
	}
	
	
	public IndexedContainer getRevisedContainer(){
		return revised_container;
	}
		
	public void openFreqsWindow(){
    	FreqsListWindow flw = new FreqsListWindow(this);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
    }
	
	public void removeSelectedFreq(Collection<Object> oList){
		//revised_container = org_container;
		
		for(Object o : oList){
			Item item = org_container.getItem(o);
			if(item != null){
				revised_container.removeItem(o);
			}
		}
		
		update(revised_container);
		
	}
	
	public void update(IndexedContainer container){
		this.revised_container = container;
		
		removeComponent(freqList);
		freqList = new Grid("Frequency");
		freqList.setContainerDataSource(container);
		configure();
		
		freqList.removeColumn("id");
		addComponent(freqList);
        
	}
	
	public void deleteSelectedFreq(){
	
	    // Delete all selected data items
	    for (Object itemId: freqList.getSelectedRows())
	    	freqList.getContainerDataSource().removeItem(itemId);

	    // Otherwise out of sync with container
	    freqList.getSelectionModel().reset();

	    // Disable after deleting
	    //e.getButton().setEnabled(false);
	
	    //e.getButton().setEnabled(freqList.getSelectedRows().size() > 0);
	}
	
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

}
