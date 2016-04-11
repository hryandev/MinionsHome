package com.rex.core.views;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.rex.backend.entity.Job;
import com.rex.core.ReportEngineUI;
import com.rex.core.components.JobPanel;
import com.rex.core.components.StringToSqlDateConverter;
import com.rex.core.forms.JobForm;
import com.rex.core.forms.MailForm;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Ryan Hsu
 */

@Theme("valo")
@Title("Job")
public class JobView extends HorizontalLayout implements View{
	private static final long serialVersionUID = -7993072104270238504L;
	
	public static final String NAME = "job";
	
	TextField filter = new TextField();
    public Grid jobList = new Grid();
    Button newContact = new Button();

    JobForm jobForm = null;
    //Panel rightPanel;
    JobPanel rightPanel = null;
    
    private JPAContainer<Job> job;
    
    private HorizontalSplitPanel sp = null;
    
	public JobView(){
		//jobForm = new JobForm(this);
		
		//rightPanel = new JobPanel(jobForm);
		//EntityManager em = Persistence.createEntityManagerFactory("reportengine").createEntityManager();
    	//em.getEntityManagerFactory().getCache().evict(Job.class);
		job = JPAContainerFactory.make(Job.class,
	               ReportEngineUI.PERSISTENCE_UNIT);
		
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
       //newContact.addClickListener(e -> jobForm.add(new Job()));
       newContact.addClickListener(e -> add(new Job()));
       
       filter.setInputPrompt("Filter jobs...");
       filter.addTextChangeListener(e -> updateFilters(e.getText()));

       //jobList.setContainerDataSource(new BeanItemContainer<>(Contact.class));
       
       jobList.setContainerDataSource(job);
       
       jobList.setColumnOrder("jobName", "nextExecTime", "activate");
       
       jobList.setImmediate(true);
       
       jobList.getColumn("nextExecTime").setConverter(new StringToDateConverter() {
    	   
    	   @Override
    	   public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale){
    		   if (targetType != getPresentationType()) {
    	            throw new ConversionException("Converter only supports " + getPresentationType().getName() + " (targetType was " + targetType.getName() + ")");
    	        }

    	        if (null == value)
    	            return null;
    			
    	        Timestamp ts = new Timestamp(value.getTime());
    	        
    	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(ts);
    	   }

       });
       
       //jobList.getColumn("nextExecTime").setConverter(new StringToSqlDateConverter());
       
       jobList.removeColumn("id");
       jobList.removeColumn("freqs");
       jobList.removeColumn("jobDesc");
       jobList.removeColumn("jobMacro");
       jobList.removeColumn("flag");
       jobList.removeColumn("jobQtm");
       jobList.removeColumn("mailSubject");
       jobList.removeColumn("mailBody");
       
       jobList.setSelectionMode(Grid.SelectionMode.SINGLE);
       //jobList.addSelectionListener(e -> jobForm.edit(jobList.getSelectedRow()));
       jobList.addSelectionListener(e -> edit(jobList.getSelectedRow()));
       
       refreshJobs();
       
	}

	private void buildLayout() {
		HorizontalLayout actions = new HorizontalLayout(filter, newContact);
		
		actions.setWidth("100%");
		filter.setWidth("100%");
		actions.setExpandRatio(filter, 1);

		
		newContact.setIcon(FontAwesome.PLUS);
		
		VerticalLayout left = new VerticalLayout(actions, jobList);
		left.setSizeFull();
		jobList.setSizeFull();
		left.setExpandRatio(jobList, 1);
		
		//VerticalLayout right = new VerticalLayout();
		//right.addComponent(jobForm);
		//right.setSizeFull();
		
		/*HorizontalLayout panelCaption = new HorizontalLayout();
		panelCaption.setMargin(true);
		panelCaption.setSpacing(true);
		panelCaption.addComponent(new Label("Test Panel Caption"));
		panelCaption.addComponent(new Button("Button1"));*/
		
		//rightPanel.setWidth("100%");
		//rightPanel.setContent(jobForm);
		//rightPanel.setVisible(false);
		
		//HorizontalSplitPanel sp = new HorizontalSplitPanel(left, rightPanel);		
		sp = new HorizontalSplitPanel(left, null);
		sp.setSizeFull();
		sp.setSplitPosition(30);
		
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
		updateFilters(filter.getValue());
    }
	
	public void destroyJobPanel(){
		rightPanel = null;
	}
	
	public HorizontalSplitPanel getSplitPanel(){
		return sp;
	}
    
    private void updateFilters(String stringFilter) {
        job.setApplyFiltersImmediately(false);
        job.removeAllContainerFilters();
        
        if (stringFilter != null && !stringFilter.equals("")) {
            Or or = new Or(new Like("jobName", stringFilter + "%", false),
                    new Like("jobMacro", stringFilter + "%", false));
            job.addContainerFilter(or);
        }
        job.applyFilters();
        
        if(jobForm != null){
        	jobForm.setVisible(false);
        }
        
        if(rightPanel != null){
        	rightPanel.setVisible(false);
        }
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
    
    public void add(Job job) {
    	if (sp.getComponentCount() == 2) {
			sp.removeComponent(rightPanel);
		}
    	
		rightPanel = new JobPanel(this, new JobForm(this, job), new MailForm(this, job), true);
		
		sp.addComponent(rightPanel);
		
		rightPanel.setCaption("New Job");
        
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);
        
        rightPanel.getJobForm().freqTable.update(container);
    	
        /*if(job != null) {
        	rightPanel.getJobForm().formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, rightPanel.getJobForm());
            job.setFlag("N");
            job.setJobQtm(120);
        }*/
        
        rightPanel.setJob(job);
        //rightPanel.getJobForm().setJob(job);
        //rightPanel.getMailForm().setJob(job);
        
        rightPanel.setVisible(job != null);
    }
    
    public void edit(Object item){    	
		if (null != item) {
			if (sp.getComponentCount() == 2) {
				sp.removeComponent(rightPanel);
			}

			Item jobItem = getJob().getItem(item);

			if (jobItem != null) {
				// rightPanel = new JobPanel(this, new JobForm(this, false));
				String jobid = (String) jobItem.getItemProperty("id").getValue();
				Job job = ((EntityItem<Job>) jobItem).getEntity();
				rightPanel = new JobPanel(this, new JobForm(this, job), new MailForm(this, job), false);

				sp.addComponent(rightPanel);

				rightPanel.setCaption("Job - " + jobItem.getItemProperty("jobName").getValue());

				rightPanel.setJob(job);

				rightPanel.getJobForm().edit(jobItem);
				rightPanel.bindFields(jobItem);
			}

			// rightPanel.getJobForm().setVisible(true);
			rightPanel.setVisible(true);
		}
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		job.refresh();
		jobList.select(null);
		if(null != rightPanel){
			rightPanel.setVisible(false);
		}
	}

}

