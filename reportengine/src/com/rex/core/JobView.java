package com.rex.core;

import com.rex.backend.entity.Job;
import com.rex.core.forms.JobForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Title("Job")
public class JobView extends HorizontalLayout implements View{
	private static final long serialVersionUID = -7993072104270238504L;
	
	TextField filter = new TextField();
    public Grid jobList = new Grid();
    Button newContact = new Button("New job");

    JobForm jobForm;
    Panel rightPanel;
    
    private JPAContainer<Job> job;
    
    //public JobService service = JobService.createDemoService();
	
	public JobView(){
		jobForm = new JobForm(this);
		rightPanel = new Panel();
		job = JPAContainerFactory.make(Job.class,
	               ReportEngineUI.PERSISTENCE_UNIT);
		//job = new HierarchicalJobContainer();
		configureComponents();
	    buildLayout();
	}
	
	private void configureComponents() {
        /* Synchronous event handling.
        *
        * Receive user interaction events on the server-side. This allows you
        * to synchronously handle those events. Vaadin automatically sends
        * only the needed changes to the web page without loading a new page.
        */
       newContact.addClickListener(e -> jobForm.add(new Job()));
       
       //newContact.addClickListener(e -> jobForm.addEntity(new Job()));
       
       filter.setInputPrompt("Filter jobs...");
       filter.addTextChangeListener(e -> updateFilters(e.getText()));

       //jobList.setContainerDataSource(new BeanItemContainer<>(Contact.class));
       
       jobList.setContainerDataSource(job);
       
       //jobList.setColumnOrder("firstName", "lastName", "email");
       
       jobList.setColumnOrder("jobName", "jobDesc", "jobMacro");
       
       jobList.removeColumn("id");
       jobList.removeColumn("freqs");
       //jobList.removeColumn("jobQuantum");
       //jobList.removeColumn("jobFreq");
       
       jobList.setSelectionMode(Grid.SelectionMode.SINGLE);
       //jobList.addSelectionListener(e
       //        -> jobForm.edit((job)jobList.getSelectedRow()));
       jobList.addSelectionListener(e
               -> jobForm.edit(jobList.getSelectedRow()));
       
       refreshJobs();
	}

	private void buildLayout() {
		HorizontalLayout actions = new HorizontalLayout(filter, newContact);
		actions.setWidth("100%");
		filter.setWidth("100%");
		actions.setExpandRatio(filter, 1);

		VerticalLayout left = new VerticalLayout(actions, jobList);
		left.setSizeFull();
		jobList.setSizeFull();
		left.setExpandRatio(jobList, 1);
		
		VerticalLayout right = new VerticalLayout();
		right.addComponent(jobForm);
		right.setSizeFull();
		
		/*HorizontalLayout panelCaption = new HorizontalLayout();
		panelCaption.setMargin(true);
		panelCaption.setSpacing(true);
		panelCaption.addComponent(new Label("Test Panel Caption"));
		panelCaption.addComponent(new Button("Button1"));*/
		
		rightPanel.setWidth("700px");
		rightPanel.setContent(jobForm);
		rightPanel.setVisible(false);
		
		HorizontalSplitPanel sp = new HorizontalSplitPanel(left, rightPanel);
		sp.setSizeFull();
		sp.setSplitPosition(50);
		
		//HorizontalLayout mainLayout = new HorizontalLayout(left, right);
		//mainLayout.setSizeFull();
		//mainLayout.setExpandRatio(left, 1);
		//mainLayout.setExpandRatio(right, 2);
		
		//this.setHeight("100%");
		setSizeFull();

		//addComponents(mainLayout);
		addComponents(sp);
		
		
		// Split and allow resizing
		//setContent(mainLayout);
	}
	

	public void refreshJobs() {
        //refreshContacts(filter.getValue());
		updateFilters(filter.getValue());
    }

    /*private void refreshContacts(String stringFilter) {
        jobList.setContainerDataSource(new BeanItemContainer<>(
                Contact.class, service.findAll(stringFilter)));
    	
        jobForm.setVisible(false);
    }*/
    
    private void updateFilters(String stringFilter) {
        job.setApplyFiltersImmediately(false);
        job.removeAllContainerFilters();
        /*if (departmentFilter != null) {
            // two level hierarchy at max in our demo
            if (departmentFilter.getParent() == null) {
                job.addContainerFilter(new Equal("department.parent",
                        departmentFilter));
            } else {
                job.addContainerFilter(new Equal("department",
                        departmentFilter));
            }
        }*/
        if (stringFilter != null && !stringFilter.equals("")) {
            Or or = new Or(new Like("jobName", stringFilter + "%", false),
                    new Like("jobMacro", stringFilter + "%", false));
            job.addContainerFilter(or);
        }
        job.applyFilters();
        
        jobForm.setVisible(false);
        rightPanel.setVisible(false);
    }
    
    public JPAContainer<Job> getJob(){
    	return job;
    }
    
    public Panel getJobPanel(){
    	return rightPanel;
    }
    
    public Grid getJobList(){
    	return jobList;
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	

}

