package com.rex.core.components;

import com.rex.backend.entity.Job;
import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;

public class JobListWindow<T> extends CustomListWindow<T> {
	private static final long serialVersionUID = 6013112066178362148L;

	public JobListWindow(CustomTable cTable) {
		super(cTable);
		// TODO Auto-generated constructor stub
		entity = (JPAContainer<T>) JPAContainerFactory.make(Job.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
		
		cList.setContainerDataSource(entity);
		
		cList.setColumnOrder("jobName", "jobDesc");
		cList.removeColumn("id");
		cList.removeColumn("freqs");
		cList.removeColumn("jobMacro");
		cList.removeColumn("flag");
		cList.removeColumn("jobQtm");
		cList.removeColumn("mailSubject");
		cList.removeColumn("mailBody");
		
		cList.setSelectionMode(Grid.SelectionMode.MULTI);
		
		setCaption("Select Job");
		
		configureComponents();
	}
	
	public void configureComponents(){
		for (int i = 0; i < orgContainer.size(); i++) {
			Item item = orgContainer.getItem(i + 1);
			
			if (item != null) {
				Property<String> customID = item.getItemProperty("id");
				Filter filter = new Not(new SimpleStringFilter("id", customID.getValue(), true, false));
				entity.addContainerFilter(filter);
			}
		}
		
	}
	
	public IndexedContainer getJobList(){
    	int j = orgContainer.size();
    	for(Object o : cList.getSelectedRows()){
    		Job job = (Job) entity.getItem(o).getEntity();
    		Item item = orgContainer.addItem(j+1);
    		item.getItemProperty("id").setValue(job.getId());
        	item.getItemProperty("jobName").setValue(job.getJobName());
        	item.getItemProperty("jobDesc").setValue(job.getJobDesc());
    		
        	j++;
    	}
    	
    	return orgContainer;
    }
	
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		if (event.getButton() == saveButton) {       	
        	cTable.update(getJobList());
            
        } else if (event.getButton() == cancelButton) {
        	
        }
        close();
	}
	
	

}
