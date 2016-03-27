package com.rex.core.views;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.rex.backend.entity.Job;
import com.rex.backend.entity.Task;
import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;


/**
 * 
 * @author Ryan Hsu
 */

@Theme("valo")
@Title("Task")
public class TaskView extends HorizontalLayout implements View{
	private static final long serialVersionUID = -6467889953177179133L;

	
	TextField filter = new TextField();
    public Grid taskList = new Grid();
    public Table tasksList = new Table();

    private JPAContainer<Task> taskContainer;
    
    private static final String[] VISIBLE_COLS = {"taskName", "jobName", "taskStatus", "startTime", "endTime", "taskFile"};
    

    public TaskView(){
    	
    	//EntityManager em = Persistence.createEntityManagerFactory("reportengine").createEntityManager();
    	
    	//em.getEntityManagerFactory().getCache().evict(Task.class);
    	taskContainer = JPAContainerFactory.make(Task.class,
	               ReportEngineUI.PERSISTENCE_UNIT);
    	
		//taskList.setContainerDataSource(task);
    	//initConnectionPool();
    	//initContainer();
		configureComponents();
    }
    
    private void configureComponents() {
    	//taskList.removeColumn("id");
    	//taskList.removeColumn("job");
    	//taskList.removeColumn("jobID");
    	//taskList.setSelectionMode(Grid.SelectionMode.SINGLE);
    	//taskList.addSelectionListener(e -> jobForm.edit(jobList.getSelectedRow()));
    	
    	initTaskList();
    	
    	tasksList.setVisibleColumns(VISIBLE_COLS);
    	
    	//taskList.setImmediate(true);
    	
    	addComponent(tasksList);
    	
    	tasksList.setSizeFull();
    	setSizeFull();
    }
    
    public int getTaskCount(){
    	return taskContainer.getItemIds().size();
    }
    
    private void initTaskList(){
    	tasksList.setContainerDataSource(taskContainer);
    	tasksList.addGeneratedColumn("jobName", new ColumnGenerator(){
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				
				Item item = source.getItem(itemId);
				Job job = (Job)item.getItemProperty("job").getValue();
				String jobName = job.getJobName();
				
				return jobName;
			}
    		
    	});
    	
    	tasksList.setVisibleColumns(VISIBLE_COLS);
    	tasksList.setSelectable(true);
    	tasksList.setImmediate(true);
    }
    
    public void showError(String errorString) {
    	Notification.show(errorString, Type.ERROR_MESSAGE);
    }
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		//EntityManager em = Persistence.createEntityManagerFactory("reportengine").createEntityManager();
    	//em.getEntityManagerFactory().getCache().evict(Task.class);
		
	}
	
}
