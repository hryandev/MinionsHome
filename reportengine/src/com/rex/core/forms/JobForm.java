package com.rex.core.forms;

import com.rex.backend.entity.FreqTest;
import com.rex.backend.entity.JobTest;
import com.rex.core.JobView;
import com.rex.core.ReportEngineUI;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
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
    
    private Table freqTable;
    
    private JobTest job;
    private JobView jobView;
    private boolean addFlag = false;
    
    FieldGroup binder;

    // Easily bind forms to beans and manage validation and buffering
    private BeanFieldGroup<JobTest> formFieldBindings;
    private JPAContainer<FreqTest> freqs;

    public JobForm(JobView jobView) {
    	this.jobView = jobView;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
    	freqs = JPAContainerFactory.make(FreqTest.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
    	freqTable = new Table("Frequency", freqs);
    	freqTable.setSelectable(true);
    	freqTable.setImmediate(true);
    	
    	freqTable.setVisibleColumns(new Object[] {"freqName", "freqDesc"});
    	
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
        
        delButton.addClickListener(e -> freqs.removeItem(freqTable.getValue()));
        
        tableArea.addComponent(toolbar);
        tableArea.addComponent(freqTable);
        tableArea.setExpandRatio(freqTable, 1);

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
        jobView.jobList.select(null);
    }

    public void add(JobTest job) {
        this.job = job;
        addFlag = true;
        if(job != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
            jobName.focus();
        }
        setVisible(job != null);
    }
    
    public void edit(Object item){
    	Item jobItem = jobView.getJob().getItem(item);
    	if(jobItem != null) {
    		binder = new FieldGroup(jobItem);
    		binder.setBuffered(true);
    		
    		binder.bind(jobName, "jobName");
    		binder.bind(jobDesc, "jobDesc");
    		binder.bind(jobMacro, "jobMacro");
    		
    		//formFieldBindings = BeanFieldGroup.bindFieldsBuffered(jobItem, this);
    		//formFieldBindings = new BeanFieldGroup<JobTest>(JobTest.class);
    		
    		jobName.focus();
    	}
    	setVisible(jobItem != null);
    	
    }
    
    public JobTest getJob(){
    	return job;
    }

}
