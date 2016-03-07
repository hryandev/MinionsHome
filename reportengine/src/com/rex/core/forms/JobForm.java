package com.rex.core.forms;

import com.rex.backend.entity.Job;
import com.rex.core.JobView;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class JobForm extends FormLayout {
	private static final long serialVersionUID = -9095274908375460436L;
	
	Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField job_Name = new TextField("Job Name");
    TextField job_Desc = new TextField("Job Description");
    TextField job_Macro = new TextField("Job Macro");
    //TextField job_Qty = new TextField("Job Quantum");
    //DateField job_Freq = new DateField("Job Frequency");

    Job job;
    
    JobView jobView;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<Job> formFieldBindings;

    public JobForm(JobView job) {
    	this.jobView = job;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        /* Highlight primary actions.
         *
         * With Vaadin built-in styles you can highlight the primary save button
         * and give it a keyboard shortcut for a better UX.
         */
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

		addComponents(actions, job_Name, job_Desc, job_Macro);
    }

    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();
            
            // Save DAO to backend with direct synchronous service API
            //getUI().service.save(contact);
            //jobView.service.save(_job);
            jobView.getJob().addEntity(job);

            String msg = String.format("Saved '%s %s'.",
            		job.getJob_Name(),
                    job.getJob_Macro());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            jobView.refreshJobs();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        jobView.jobList.select(null);
    }

    public void edit(Job job) {
        this.job = job;
        if(job != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
            job_Name.focus();
        }
        setVisible(job != null);
    }


}
