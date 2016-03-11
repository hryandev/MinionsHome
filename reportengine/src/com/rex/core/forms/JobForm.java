package com.rex.core.forms;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.backend.service.JobService;
import com.rex.core.JobView;
import com.rex.core.components.FreqTable;
import com.rex.core.components.FreqsListWindow;
import com.rex.core.components.FreqsListWindow.EditorSavedEvent;
import com.rex.core.components.FreqsListWindow.EditorSavedListener;
import com.rex.core.components.RefreshableBeanItemContainer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class JobForm extends FormLayout {
	private static final long serialVersionUID = -9095274908375460436L;
	
	Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField jobName = new TextField("Job Name");
    TextField jobDesc = new TextField("Job Description");
    TextField jobMacro = new TextField("Job Macro");
    CheckBox jobFlag = new CheckBox("Activate");
    
    public FreqTable freqTable;
    
    private Job job;
    private JobView jobView;
    private boolean addFlag = false;
    
    FieldGroup binder;

    // Easily bind forms to beans and manage validation and buffering
    private BeanFieldGroup<Job> formFieldBindings;
    private JPAContainer<Freq> freqs;
    //private BeanItemContainer<Freq> freqContainer;
    
    public RefreshableBeanItemContainer<Freq> freqContainer;

    public JobForm(JobView jobView) {
    	this.jobView = jobView;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
    	freqTable = new FreqTable();
    	
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);
        
        VerticalLayout tableArea = new VerticalLayout();
        HorizontalLayout toolbar = new HorizontalLayout();
        Button newButton = new Button("Add");
        Button delButton = new Button("Delete");
        toolbar.addComponent(newButton);
        toolbar.addComponent(delButton);
        
        newButton.addClickListener(e -> openFreqsWindow());
        //delButton.addClickListener(e -> freqs.removeItem(freqTable.getValue()));
        
        tableArea.addComponent(toolbar);
        tableArea.addComponent(freqTable);
        
		addComponents(actions, jobName, jobDesc, jobMacro, jobFlag, tableArea);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
    }

    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
        	if(addFlag){
        		formFieldBindings.commit();
        		jobView.getJob().addEntity(job);
        		jobView.getJob().commit();
        		
        	}
        	else{
        		binder.commit();
        	}
            
            // Save DAO to backend with direct synchronous service API
            //getUI().service.save(contact);
            //jobView.service.save(_job);
            //jobView.getJob().addEntity(job);

            String msg = String.format("Saved '%s %s'.",
            		jobName.getValue(),
            		jobMacro.getValue());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            jobView.refreshJobs();
            addFlag = false;
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        
        //jobView.jobList.select(null);
        //jobView.getJobList().select(null);
        jobView.getJobPanel().setVisible(false);
    }

    public void add(Job job) {
    	jobView.getJobPanel().setVisible(true);
    	jobView.getJobPanel().setCaption("New Job");
        this.job = job;
        addFlag = true;
        
        //removeComponent(freqTable);
    	//freqTable = new FreqTable(new IndexedContainer());
    	freqTable.update(new IndexedContainer());
    	//addComponent(freqTable);
    	
        if(job != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
            jobName.focus();
        }
        setVisible(job != null);
        //jobView.getJobPanel().setVisible(job != null);
    }
    
    public void edit(Object item){
		jobView.getJobPanel().setVisible(true);
		Item jobItem = jobView.getJob().getItem(item);
		job = jobView.getJob().getItem(item).getEntity();
		jobView.getJobPanel().setCaption("Job - " + job.getJobName());

		//removeComponent(freqTable);
		//freqTable = new FreqTable(getFreqs(job));
		freqTable.update(getFreqs(job));
		//addComponent(freqTable);

		if (jobItem != null) {
			// formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
			binder = new FieldGroup(jobItem);
			binder.setBuffered(true);

			binder.bind(jobName, "jobName");
			binder.bind(jobDesc, "jobDesc");
			binder.bind(jobMacro, "jobMacro");

			jobName.focus();
		}
		setVisible(jobItem != null);
		// jobView.getJobPanel().setVisible(jobItem != null);
    }
    
    public void openFreqsWindow(){
    	FreqsListWindow flw = new FreqsListWindow(job, freqTable);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
        //setEnabled(false);
 
    	//flw.addListener(new EditorSavedListener() {
        //    public void editorSaved(EditorSavedEvent event) {
            	//freqContainer.addAll(flw.getFreqList());
            	
				/*for (Iterator iterator = flw.getFreqList().iterator(); iterator.hasNext();) {
					Freq item = (Freq) iterator.next();
					
					freqContainer.addBean(item);
				}*/
            	
				//freqContainer.refreshItems();
				//freqTable.setEnabled(true);
				//freqTable.getFreqList().setContainerDataSource(getFreqs(job));
        //    }
        //});
    }
    
    /*public void openFreqsWindow(Job job){
    	
    	FreqsListWindow flw = new FreqsListWindow(job);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
    }*/
    
    public IndexedContainer getFreqs(Job job){
    	IndexedContainer container = new IndexedContainer();
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);
        
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("reportengine");
        //EntityManager em = emf.createEntityManager();

        JobService jobService = new JobService();
        
        Job _job = jobService.findJob(job.getId());
        
        if(_job != null){
	        for(int i = 1; i< _job.getFreqs().size()+1; i++){
	        	Item item = container.addItem(i);
	        	item.getItemProperty("freqName").setValue(_job.getFreqs().get(i-1).getFreqName());
	        	item.getItemProperty("freqDesc").setValue(_job.getFreqs().get(i-1).getFreqDesc());
	        }
        }
        
        return container;
    }
    
    public Job getJob(){
    	return job;
    }

}
