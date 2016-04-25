package com.rex.core.components;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
	private IndexedContainer jobList;
	
	private LinkedList<Job> remainder = new LinkedList<>(); 
	
	private final String[] COLUMNS = {"id", "jobName", "jobDesc", "issuer", "issueTime"};

	public JobListWindow(CustomTable cTable) {
		super(cTable);
		// TODO Auto-generated constructor stub
		entity = (JPAContainer<T>) JPAContainerFactory.make(Job.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
		
		initJobList();
		
		cList.setContainerDataSource(jobList);
		
		cList.setColumnOrder(COLUMNS);
		cList.removeColumn("id");
		//cList.removeColumn("freqs");
		//cList.removeColumn("jobMacro");
		//cList.removeColumn("flag");
		//cList.removeColumn("jobQtm");
		//cList.removeColumn("mailSubject");
		//cList.removeColumn("mailBody");
		//cList.removeColumn("issuer");
		cList.removeColumn("issueTime");
		//cList.removeColumn("tasks");
		
		cList.setSelectionMode(Grid.SelectionMode.MULTI);
		
		setCaption("Select Job");
		
		//configureComponents();
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
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("id", String.class, null);
		container.addContainerProperty("jobName", String.class, null);
        container.addContainerProperty("jobDesc", String.class, null);
        container.addContainerProperty("issuer", String.class, null);
        container.addContainerProperty("issueTime", Date.class, null);
        
    	int j = 0;
    	
    	for(Object o : cList.getSelectedRows()){
    		Item job = jobList.getItem(o);
    		Item item = container.addItem(++j);
    		
    		for(int i = 0; i < COLUMNS.length; i++){
    			
    			item.getItemProperty(COLUMNS[i]).setValue(job.getItemProperty(COLUMNS[i]).getValue());
        	}
    	}
    	
    	for(Job job : remainder){
    		Item it = container.addItem(++j);
    		
    		it.getItemProperty("id").setValue(job.getId());
    		it.getItemProperty("jobName").setValue(job.getJobName());
    		it.getItemProperty("jobDesc").setValue(job.getJobDesc());
    		it.getItemProperty("issuer").setValue(job.getIssuer());
    		it.getItemProperty("issueTime").setValue(job.getIssueTime());
    	}
    	
    	return container;
    }
	
	public void initJobList() {
		jobList = new IndexedContainer();
		jobList.addContainerProperty("id", String.class, null);
		jobList.addContainerProperty("jobName", String.class, null);
		jobList.addContainerProperty("jobDesc", String.class, null);
		jobList.addContainerProperty("issuer", String.class, null);
		jobList.addContainerProperty("issueTime", Date.class, null);
		
		List<String> freqOrgList = new LinkedList<>();
		
		if(orgContainer.size() != 0){
			for (Iterator i = orgContainer.getItemIds().iterator(); i.hasNext();) {
			//for(int j = 0; j < orgContainer.getItemIds().size(); j++){
				int iid = (int) i.next();
				//Object o = orgContainer.getItemIds().iterator();
				Item orgItem = orgContainer.getItem(iid);
				System.out.println("orgItem = "+orgItem);
				
				if(null != orgItem){
					Property<String> freqID = orgItem.getItemProperty("id");
					freqOrgList.add(freqID.getValue());
				}
			}
		}
		
		int i = 0;
		for (Object o : entity.getItemIds()) {
			Job job = (Job) entity.getItem(o).getEntity();
			
			if (!freqOrgList.contains(job.getId())) {
				Item item = jobList.addItem(++i);
				item.getItemProperty("id").setValue(job.getId());
				item.getItemProperty("jobName").setValue(job.getJobName());
				item.getItemProperty("jobDesc").setValue(job.getJobDesc());
				item.getItemProperty("issuer").setValue(job.getIssuer());
				item.getItemProperty("issueTime").setValue(job.getIssueTime());
			}else{
				remainder.add(job);
			}
			
			
		}
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
