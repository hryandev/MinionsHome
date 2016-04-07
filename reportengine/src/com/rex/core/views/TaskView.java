package com.rex.core.views;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rex.backend.entity.Job;
import com.rex.backend.entity.Task;
import com.rex.components.valo.Icons;
import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
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
    
    private static Map<String, String> COLUMNS = new LinkedHashMap<String, String>();
    
    private final String SAVE_FOLDER = "ExcelModule";
    private final String ACCESS_PATH = "\\\\163.50.47.14\\rex\\";

    public TaskView(){
    	taskContainer = JPAContainerFactory.make(Task.class,
	               ReportEngineUI.PERSISTENCE_UNIT);
    	
    	//initConnectionPool();
    	//initContainer();
    	initColumn();
		configureComponents();
    }
    
    private void configureComponents() {
    	initTaskList();
    	
    	addComponent(tasksList);
    	
    	tasksList.setSizeFull();
    	setSizeFull();
    }
    
    public int getTaskCount(){
    	return taskContainer.getItemIds().size();
    }
    
    private void initTaskList(){
    	tasksList.setContainerDataSource(taskContainer);
    	tasksList.addGeneratedColumn("jobDesc", new ColumnGenerator(){
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				
				Item item = source.getItem(itemId);
				Job job = (Job)item.getItemProperty("job").getValue();
				String jobDesc = job.getJobDesc();
				
				return jobDesc;
			}
    		
    	});
    	
    	tasksList.addGeneratedColumn("status", new ColumnGenerator(){
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				
				String val = (String) source.getItem(itemId).getItemProperty("taskStatus").getValue();
				String status = "";
				
				switch(val){
					case "R":
						status = "Running";
						break;
					case "C":
						status = "Complete";
						break;
					case "E":
						status = "Error";
						break;
					default:
						break;
					
				}
				
				return status;
			}
    	});
    	
    	tasksList.addGeneratedColumn("link", new ColumnGenerator() {

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
            	
                String url = (String) source.getItem(itemId).getItemProperty("taskFile").getValue();
                
                int col = url.indexOf(SAVE_FOLDER);
                
                url = url.substring(col);
                //url = url.substring(7);
                String svrPath = ACCESS_PATH + url;
                
                File file = new File(svrPath);
                
                Link link = new Link(null, new FileResource(file));
                link.setIcon(new Icons("file-excel").get());
                
                //Button link = new Button();
                //link.setStyleName(Runo.BUTTON_LINK); // use the theme you are currently extending here
                //BrowserWindowOpener opener = new BrowserWindowOpener(new ExternalResource(url));
                //opener.extend(link);
                
                return link;
            }
        });
    	
    	for(String key : COLUMNS.keySet()){
    		tasksList.setColumnHeader(key, COLUMNS.get(key));
    	}
    	
    	tasksList.setColumnAlignment("link", Align.CENTER);
    	tasksList.setColumnWidth("link", 50);
    	
    	tasksList.setVisibleColumns(COLUMNS.keySet().toArray());
    	tasksList.setSelectable(true);
    	tasksList.setImmediate(true);
    }
    
    public void initColumn(){
    	COLUMNS.put("taskName", "Task Name");
    	COLUMNS.put("status", "Task Status");
    	COLUMNS.put("startTime", "Start Time");
    	COLUMNS.put("endTime", "End Time");
    	COLUMNS.put("link", "Link");
    	
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
