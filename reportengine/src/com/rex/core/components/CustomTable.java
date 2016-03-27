package com.rex.core.components;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class CustomTable extends VerticalLayout{
	private static final long serialVersionUID = 6882830661170658239L;
	
	Grid gridList;
	IndexedContainer revised_container = new IndexedContainer();
	public IndexedContainer org_container = new IndexedContainer();
	
	String caption = "";
	
	public CustomTable(String caption){
		this.caption = caption;
		gridList = new Grid(caption);
		
		configure();
		buildLayout();
	}
	
	public void configure(){
		gridList.setSelectionMode(SelectionMode.MULTI);
		gridList.setImmediate(true);
		
		/*for (Column c : gridList.getColumns()) {
            if (!c.getPropertyId().equals("icon")) {
                c.setSortable(true);
            }
            c.setHidable(true);
        }*/
		
		gridList.setColumnReorderingAllowed(true);
		gridList.setHeightByRows(6);
		
	}
	
	public void buildLayout(){
		HorizontalLayout toolbar = new HorizontalLayout();
        Button newButton = new Button("Add");
        Button delButton = new Button("Delete");
        toolbar.addComponent(newButton);
        toolbar.addComponent(delButton);
        
        addComponent(toolbar);
        addComponent(gridList);
        
        //setExpandRatio(toolbar, 1);
        
        newButton.addClickListener(e -> openCustomWindow());
        delButton.addClickListener(e -> removeSelectedObject(gridList.getSelectedRows()));
        
        setSizeFull();
		
		//setMargin(true);
		setSpacing(true);
	}
	
	public void openCustomWindow(){
    }
	
	public void removeSelectedObject(Collection<Object> oList){
		
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
		
		removeComponent(gridList);
		gridList = new Grid(caption);
		gridList.setContainerDataSource(container);
		
		gridList.removeColumn("id");
		addComponent(gridList);
        
	}
	
	public Grid getGridList(){
		return gridList;
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
}
