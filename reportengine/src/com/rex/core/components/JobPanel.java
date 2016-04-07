package com.rex.core.components;

import com.rex.backend.entity.Job;
import com.rex.components.valo.Icons;
import com.rex.core.forms.JobForm;
import com.rex.core.forms.MailForm;
import com.rex.core.views.JobView;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class JobPanel extends Panel{
	private static final long serialVersionUID = 5819965722042216078L;

	private JobForm jobForm;
	private MailForm mailForm;
	private TestForm testForm;
	private TabSheet tabs;
	
	private JobView jobView;
	private boolean addFlag;
	
	private Job job;
	public FieldGroup binder;

	Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);

    public JobPanel(JobView jView){
		super();
		this.jobView = jView;
		jobForm = new JobForm(jobView);
		mailForm = new MailForm();
		testForm = new TestForm();
		configureComponents();
		buildLayout();
	}
    
	public JobPanel(JobForm jobForm){
		super();
		this.jobForm = jobForm;
		mailForm = new MailForm();
		testForm = new TestForm();
		configureComponents();
		buildLayout();
	}
	
	public JobPanel(JobView jView, JobForm jForm){
		super();
		this.jobView = jView;
		this.jobForm = jForm;
		mailForm = new MailForm();
		testForm = new TestForm();
		configureComponents();
		buildLayout();
	}
	
	public JobPanel(JobView jView, JobForm jForm, MailForm mForm, boolean addFlag){
		super();
		this.jobView = jView;
		this.jobForm = jForm;
		this.mailForm = mForm;
		
		this.addFlag = addFlag;
		
		jobForm.addFlag = addFlag;
		mailForm.addFlag = addFlag;
		
		configureComponents();
		buildLayout();
	}
	
	private void configureComponents(){
		tabs = new TabSheet();
		
		Tab tabJob = tabs.addTab(jobForm, "Job Detail");
		Tab tabMail = tabs.addTab(mailForm);
		//Tab tabTest = tabs.addTab(testForm, "Test");
		
		tabJob.setIcon(new Icons("checkbox-checked").get());
		tabJob.setClosable(false);
		tabJob.setEnabled(true);
		
		
		tabMail.setIcon(new Icons("envelop").get());
		tabMail.setClosable(false);
		tabMail.setEnabled(true);
		
		tabs.addSelectedTabChangeListener(e -> {
			String caption = e.getTabSheet().getSelectedTab().getCaption();
			
			if("Mail".equals(caption)){
				mailForm.buildAddresses();
			}
		});
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        //save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
	}
	
	private void buildLayout(){
		VerticalLayout content = new VerticalLayout();
		
		//HorizontalLayout toolbar = new HorizontalLayout();
		
		GridLayout toolbar = new GridLayout(2, 1);
		toolbar.setWidth("100%");
		
		toolbar.setSpacing(true);
		
		//Label blank = new Label("");
		//blank.setWidth("950px");
		
		toolbar.addComponent(save, 0, 0);
		toolbar.addComponent(cancel, 1, 0);
		
		toolbar.setColumnExpandRatio(0, 3);
		
		toolbar.setComponentAlignment(save, Alignment.MIDDLE_RIGHT);
		toolbar.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);
		
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponents(toolbar, tabs);
		
		content.setResponsive(true);
		
		tabs.addStyleName("framed");
		
		tabs.setSizeFull();
		
		//content.setSizeFull();
		
		setWidth("100%");
		setVisible(false);
		setSizeFull();
		
		setContent(content);
		
	}
	
	public TabSheet getTabSheet(){
		return tabs;
	}
	
	public JobForm getJobForm(){
		return jobForm;
	}
	
	public MailForm getMailForm(){
		return mailForm;
	}
	
	public void save(Button.ClickEvent event){
		try {
			if (addFlag) {
				
				bindFields();

				jobView.getJob().addEntity(job);
				jobView.getJob().commit();
			} else {
				binder.commit();
			}
			
			
			jobForm.saveFreqs(addFlag);
			mailForm.saveUserGroup(addFlag, "T");
			mailForm.saveUserGroup(addFlag, "C");
			
			String msg = String.format("Saved '%s'.",
            		job.getJobName());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
			
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        addFlag = false;
		
		jobView.refreshJobs();
		jobView.getJobList().deselectAll();
        
		jobView.getSplitPanel().removeComponent(this);
        jobView.destroyJobPanel();
        
	}
	
	public void bindFields(Item jobItem){
		//Item jobItem = jobView.getJob().getItem(item);
		
		if (jobItem != null) {
			binder = new FieldGroup(jobItem);
			binder.setBuffered(true);

			binder.bind(jobForm.getJobName(), "jobName");
			binder.bind(jobForm.getJobDesc(), "jobDesc");
			binder.bind(jobForm.getJobMacro(), "jobMacro");
			binder.bind(jobForm.getActivate(), "activate");
			binder.bind(mailForm.getMailSubject(), "mailSubject");
			binder.bind(mailForm.getMailBody(), "mailBody");
			
		}

	}
	
	public void bindFields(){
		job.setJobName(jobForm.getJobName().getValue());
		job.setJobDesc(jobForm.getJobDesc().getValue());
		job.setJobMacro(jobForm.getJobMacro().getValue());
		job.setActivate(jobForm.getActivate().getValue());
		
		job.setMailSubject(mailForm.getMailSubject().getValue());
		job.setMailBody(mailForm.getMailBody().getValue());
		
		job.setFlag("N");
        job.setJobQtm(120);
	}
	
	public void saveUserGroup(){
		
	}
	
	public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        //jobView.getJobList().deselectAll();
        jobView.getJobList().select(null);
        jobView.getJobPanel().setVisible(false);
        
        //jobForm.addFlag = false;
        jobView.getSplitPanel().removeComponent(this);
        jobView.destroyJobPanel();
    }
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	
}
