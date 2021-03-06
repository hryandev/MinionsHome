package com.rex.core.views;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rex.backend.entity.Job;
import com.rex.backend.entity.Task;
import com.rex.backend.entity.User;
import com.rex.backend.service.JobService;
import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;


/**
 * 
 * @author Ryan Hsu
 */


@Title("Report")
public class ReportView extends HorizontalLayout implements View{
	private static final long serialVersionUID = -6467889953177179133L;
	
	public static final String NAME = "report";
	
	TextField filter = new TextField();
    public Grid taskList = new Grid();
    public Table tasksList;
    
    private User user;

    private boolean flag = false;
    
    public IndexedContainer taskContainer = null;
    //private JPAContainer<Task> taskContainer;
    
    private static Map<String, String> COLUMNS = new LinkedHashMap<String, String>();
    
    private final String SAVE_FOLDER = "ExcelModule";
    private final String ACCESS_PATH = "\\\\163.50.47.14\\rex\\";
    //private final String ACCESS_PATH = "D:\\REX\\";

    public ReportView(){
    	//initReport();
    	initColumn();
		configureComponents();
    }

	public void configureComponents() {
    	tasksList = new Table() {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId,
                    Property property) {
                Object v = property.getValue();
                if (v instanceof Date) {
                    Date dateValue = (Date) v;
                    
                    Timestamp ts = new Timestamp(dateValue.getTime());
                    
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(ts);
                    
                }
                return super.formatPropertyValue(rowId, colId, property);
            }

        };
    	
        //initTaskList();
    	addComponent(tasksList);
    	
    	tasksList.setSizeFull();
    	setSizeFull();
    }
    
    public int getTaskCount(){
    	return taskContainer.getItemIds().size();
    }
    
    public void initTaskList(){
    	if(taskContainer != null){
	    	tasksList.setContainerDataSource(taskContainer);
	    	
	    	addGeneratedColumn();
	    	
	    	for(String key : COLUMNS.keySet()){
	    		tasksList.setColumnHeader(key, COLUMNS.get(key));
	    	}
	    	
	    	tasksList.setColumnAlignment("link", Align.CENTER);
	    	tasksList.setColumnWidth("link", 50);
	    	
	    	tasksList.setVisibleColumns(COLUMNS.keySet().toArray());
	    	tasksList.setSelectable(true);
	    	tasksList.setImmediate(true);
    	}
    	
    	this.flag = true;
    	
    }
    
    public void initColumn(){
    	COLUMNS.put("taskName", "Report Name");
    	COLUMNS.put("endTime", "Delivery Time");
    	COLUMNS.put("link", "Link");    	
    }
    
    public void showError(String errorString) {
    	Notification.show(errorString, Type.ERROR_MESSAGE);
    }
    
    public void addGeneratedColumn(){
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
                
                Link link = new Link(null, null);
                
                if(url != null && !url.isEmpty()){
                
	                int col = url.indexOf(SAVE_FOLDER);
	                
	                url = url.substring(col);
	                //url = url.substring(7);
	                String svrPath = ACCESS_PATH + url;
	                
	                File file = new File(svrPath);
	                
	                if(file.exists()){
	                
		                link = new Link(null, new FileResource(file));
		                link.setIcon(FontAwesome.FILE_EXCEL_O);
	                }
                }
                
                return link;
            }
        });
    }
    
    public void initReport(){
    		taskContainer = new IndexedContainer();
    		taskContainer.addContainerProperty("id", String.class, null);
    		taskContainer.addContainerProperty("taskName", String.class, null);
    		taskContainer.addContainerProperty("status", String.class, null);
    		taskContainer.addContainerProperty("startTime", Date.class, null);
    		taskContainer.addContainerProperty("endTime", Date.class, null);
    		taskContainer.addContainerProperty("taskFile", String.class, null);

    		List<Job> jobs = user.getJobs();
    		int i = 0;
    		for(Job job : jobs){
    			List<Task> tasks = job.getTasks();
    			for(Task task : tasks){
    				if("C".equals(task.getTaskStatus())){
	    				Item item = taskContainer.addItem(++i);
	    				item.getItemProperty("id").setValue(task.getId());
	    	        	item.getItemProperty("taskName").setValue(task.getTaskName());
	    	        	item.getItemProperty("status").setValue(task.getTaskStatus());
	    	        	item.getItemProperty("startTime").setValue(task.getStartTime());
	    	        	item.getItemProperty("endTime").setValue(task.getEndTime());
	    	        	item.getItemProperty("taskFile").setValue(task.getTaskFile());
    	        	}
    			}
    		}
    }
    
    public int getReports(){
    	if(taskContainer != null){
    		return taskContainer.getItemIds().size();
    	}
    	
    	return 0;
    }
    
    public void initUserInfo(User user){
    	this.user = user;
    }
    
    public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		//EntityManager em = Persistence.createEntityManagerFactory("reportengine").createEntityManager();
    	//em.getEntityManagerFactory().getCache().evict(Task.class);
		
		//List<Job> jobs = user.getJobs();
		
		/*if(jobs != null){
			for(Job job : jobs){
				List<Task> tasks = job.getTasks();
				if(tasks != null){
					for(Task task : tasks){
						
						System.out.println("taskid = "+task.getId());
					}
				}
			}
		}*/
		
	}
	
}
