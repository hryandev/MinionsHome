package com.rex.core.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.backend.service.JobService;
import com.rex.core.components.CountryBean;
import com.rex.core.components.DatabaseAccessService;
import com.rex.core.components.DatabaseAccessServiceImpl;
import com.rex.core.components.FreqTable;
import com.rex.core.components.FreqsListWindow;
import com.rex.core.components.SuggestingComboBox;
import com.rex.core.components.SuggestingContainer;
import com.rex.core.components.SuggestingTextField;
import com.rex.core.views.JobView;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;


/**
 * 
 * @author Ryan Hsu
 */

@Theme("valo")
public class JobForm extends FormLayout {
	private static final long serialVersionUID = -9095274908375460436L;
	
	Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField jobName = new TextField("Job Name");
    TextField jobDesc = new TextField("Job Description");
    TextField jobMacro = new TextField("Job Macro");
    CheckBox activate = new CheckBox("Activate");
    
    public FreqTable freqTable;
    public List<Freq> orgFreqs = new ArrayList<>();
    List<Freq> delList = null;
	List<Freq> addList = null;
    
    private Job job;
    private JobView jobView;
    public boolean addFlag = false;
    
    
    private SuggestingComboBox comboBox;
    private SuggestingTextField stf = new SuggestingTextField();
    final DatabaseAccessService databaseAccessService = new DatabaseAccessServiceImpl();
    
    public FieldGroup binder;

    // Easily bind forms to beans and manage validation and buffering
    public BeanFieldGroup<Job> formFieldBindings;

    
    public JobForm(JobView jobView) {
    	this.jobView = jobView;
        configureComponents();
        buildLayout();
    }
    
    public JobForm(JobView jobView, Job job) {
    	this.jobView = jobView;
    	this.job = job;
    	
        configureComponents();
        buildLayout();
    }
    
    public JobForm(JobView jobView, boolean addFlag) {
    	this.addFlag = addFlag;
    	this.jobView = jobView;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
    	freqTable = new FreqTable();
    	
    	jobName.setNullRepresentation("");
    	jobDesc.setNullRepresentation("");
    	jobMacro.setNullRepresentation("");
    	
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(true);
        
        final SuggestingContainer container = new SuggestingContainer(databaseAccessService);
        comboBox = new SuggestingComboBox("Suggesting ComboBox");
        comboBox.setImmediate(true);
        comboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Notification.show("Selected item: " + event.getProperty().getValue(), Type.HUMANIZED_MESSAGE);
                // tell the custom container that a value has been selected. This is necessary to ensure that the
                // selected value is displayed by the ComboBox
                container.setSelectedCountryBean((CountryBean) event.getProperty().getValue());
            }
        });
        comboBox.setContainerDataSource(container);
    }

    private void buildLayout() {
        //setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);
        
        /*VerticalLayout tableArea = new VerticalLayout();
        HorizontalLayout toolbar = new HorizontalLayout();
        Button newButton = new Button("Add");
        Button delButton = new Button("Delete");
        toolbar.addComponent(newButton);
        toolbar.addComponent(delButton);*/
        
        //newButton.addClickListener(e -> openFreqsWindow());
        //delButton.addClickListener(e -> freqs.removeItem(freqTable.getValue()));
        
        //tableArea.addComponent(toolbar);
        //tableArea.addComponent(freqTable);
        
        Label caption = new Label("Frequency");
        caption.addStyleName("h3");
        caption.addStyleName("colored");
        
		addComponents(jobName, jobDesc, jobMacro, activate, caption, freqTable);
		
		setSizeFull();
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
        	
        	saveFreqs();
            
            String msg = String.format("Saved '%s %s'.",
            		jobName.getValue(),
            		jobMacro.getValue());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            jobView.refreshJobs();
            addFlag = false;
            jobView.getJobList().select(null);
            jobView.getJobPanel().setVisible(false);
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        
        jobView.getJobList().select(null);
        jobView.getJobPanel().setVisible(false);
        
        addFlag = false;
        jobView.getSplitPanel().removeComponent(this);
        jobView.destroyJobPanel();
    }

    public void add(Job job) {
    	jobView.getJobPanel().setVisible(true);
    	jobView.getJobPanel().setCaption("New Job");
    	
        this.job = job;
        addFlag = true;
        
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);
        container.addContainerProperty("startTime", Date.class, null);
        
        freqTable.update(container);
    	
        if(job != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
            job.setFlag("N");
            job.setJobQtm(120);
            
            jobName.focus();
        }
        setVisible(job != null);
    }
    
    public void edit(Object item){
		//jobView.getJobPanel().setVisible(true);
		Item jobItem = jobView.getJob().getItem(item);
		
		if(jobItem != null){
			String jobid = (String)jobItem.getItemProperty("id").getValue();
			job = jobView.getJob().getItem(item).getEntity();
			//jobView.getJobPanel().setCaption("Job - " + jobItem.getItemProperty("jobName").getValue());
			freqTable.setOrgContainer(getFreqs(jobid));
			freqTable.update(getFreqs(jobid));
	
			/*if (jobItem != null) {
				binder = new FieldGroup(jobItem);
				binder.setBuffered(true);
	
				binder.bind(jobName, "jobName");
				binder.bind(jobDesc, "jobDesc");
				binder.bind(jobMacro, "jobMacro");
				binder.bind(activate, "activate");
	
				jobName.focus();
			}*/
		}
		
		setVisible(jobItem != null);
    }
    
    public void edit(Item jobItem){
    	if(jobItem != null){
			String jobid = (String)jobItem.getItemProperty("id").getValue();
			//job = jobView.getJob().getItem(item).getEntity();
			freqTable.setOrgContainer(getFreqs(jobid));
			freqTable.update(getFreqs(jobid));
		}
		
		setVisible(jobItem != null);
    }
    
    public void setJob(Job job){
    	this.job = job;
    }
    
    public void openFreqsWindow(){
    	FreqsListWindow flw = new FreqsListWindow(job, freqTable);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
    }
    
    public IndexedContainer getFreqs(String jobid){
    	IndexedContainer container = new IndexedContainer();
    	container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);
        container.addContainerProperty("startTime", Date.class, null);
        
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("reportengine");
        //EntityManager em = emf.createEntityManager();

        JobService jobService = new JobService();
        
        Job _job = jobService.findJob(jobid);
        
        if(_job != null){
        	orgFreqs = _job.getFreqs();
        	
	        for(int i = 1; i< _job.getFreqs().size()+1; i++){
	        	Item item = container.addItem(i);
	        	item.getItemProperty("id").setValue(_job.getFreqs().get(i-1).getId());
	        	item.getItemProperty("freqName").setValue(_job.getFreqs().get(i-1).getFreqName());
	        	item.getItemProperty("freqDesc").setValue(_job.getFreqs().get(i-1).getFreqDesc());
	        	item.getItemProperty("startTime").setValue(_job.getFreqs().get(i-1).getStartTime());
	        }
        }
        
        return container;
    }
    
    public Job getJob(){
    	return job;
    }
    
    public void saveFreqs(){
    	List<Freq> fList = getRevisedFreqList();
    	
    	JobService jobService = new JobService();
        
    	if(addFlag){
    		jobService.addFreqs(job.getId(), fList);
    	}else{
    		addList = new ArrayList<Freq>();
    		delList = new ArrayList<Freq>();
    		
    		for(Freq f : orgFreqs){
    		    if(!fList.contains(f)){
    		    	delList.add(f);
    		    }
        	}
        	
        	for(Freq freq : fList){
	        	if(!orgFreqs.contains(freq)){
	        		addList.add(freq);
	        	}
        	}
        	
        	if(delList != null){
        		jobService.removeFreqs(job.getId(), delList);
        	}
        	
        	if(addList != null){
        		jobService.addFreqs(job.getId(), addList);
        	}
    	}
    }
    
    public void saveFreqs(boolean addFlag){
    	List<Freq> fList = getRevisedFreqList();
    	
    	JobService jobService = new JobService();
        
    	if(addFlag){
    		jobService.addFreqs(job.getId(), fList);
    	}else{
    		addList = new ArrayList<Freq>();
    		delList = new ArrayList<Freq>();
    		
    		for(Freq f : orgFreqs){
    		    if(!fList.contains(f)){
    		    	delList.add(f);
    		    }
        	}
        	
        	for(Freq freq : fList){
	        	if(!orgFreqs.contains(freq)){
	        		addList.add(freq);
	        	}
        	}
        	
        	if(delList != null){
        		jobService.removeFreqs(job.getId(), delList);
        	}
        	
        	if(addList != null){
        		jobService.addFreqs(job.getId(), addList);
        	}
    	}
    }
    
    public List<Freq> getRevisedListAdd(){
    	List<Freq> fList = new ArrayList<>();
    	
    	for(int i = 0; i < freqTable.getRevisedContainer().size(); i++){
    		Item item = freqTable.getRevisedContainer().getItem(i+1);
    		Freq freq = new Freq();
    		
    		freq.setId((String)item.getItemProperty("id").getValue());
    		freq.setFreqName((String)item.getItemProperty("freqName").getValue());
    		freq.setFreqDesc((String)item.getItemProperty("freqDesc").getValue());
    		freq.setStartTime((Date)item.getItemProperty("startTime").getValue());
    		
    		fList.add(freq);
    	}
    	
    	return fList;
    }
    
    public List<Freq> getRevisedFreqList(){
    	List<Freq> fList = new ArrayList<>();
    	
    	Collection<Integer> tempList = (Collection<Integer>) freqTable.getRevisedContainer().getItemIds();
    	
    	for(Integer j : tempList){
    		Item item = freqTable.getRevisedContainer().getItem(j);
    		Freq freq = new Freq();
    		
    		freq.setId((String)item.getItemProperty("id").getValue());
    		freq.setFreqName((String)item.getItemProperty("freqName").getValue());
    		freq.setFreqDesc((String)item.getItemProperty("freqDesc").getValue());
    		freq.setStartTime((Date)item.getItemProperty("startTime").getValue());
    		
    		fList.add(freq);
    	}
    	
    	return fList;
    	
    }
    

	public TextField getJobName() {
		return jobName;
	}

	public void setJobName(TextField jobName) {
		this.jobName = jobName;
	}

	public TextField getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(TextField jobDesc) {
		this.jobDesc = jobDesc;
	}

	public TextField getJobMacro() {
		return jobMacro;
	}

	public void setJobMacro(TextField jobMacro) {
		this.jobMacro = jobMacro;
	}

	public CheckBox getActivate() {
		return activate;
	}

	public void setActivate(CheckBox activate) {
		this.activate = activate;
	}
    
    

}
