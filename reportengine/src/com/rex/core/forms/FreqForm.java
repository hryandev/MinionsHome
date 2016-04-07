package com.rex.core.forms;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.backend.service.FreqService;
import com.rex.core.components.JobTable;
import com.rex.core.views.FreqView;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;


/**
 * 
 * @author Ryan Hsu
 */

@Theme("valo")
public class FreqForm extends FormLayout {
	private static final long serialVersionUID = -9095274908375460436L;
	
	Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField freqName = new TextField("Name");
    TextField freqDesc = new TextField("Description");
    ComboBox freqType = new ComboBox("Type");
    CheckBox execute = new CheckBox("Execute now");
    //Component startTimeString;
    DateField startTime = new DateField();
    
    OptionGroup executeOption = new OptionGroup("Start Time");
    ComboBox interval = new ComboBox("Interval");
    ComboBox repeat = new ComboBox("Repeats");
    
    public JobTable jobTable;
    
    private static LinkedHashMap<String, String> types = new LinkedHashMap<String, String>();
    
    public List<Job> orgJobList = new ArrayList<>();
    List<Job> delList = null;
	List<Job> addList = null;
    
	private final String EXECUTE_STR = "EXEC NOW";
	
    private Freq freq;
    private FreqView freqView;
    private boolean addFlag = false;
    
    FieldGroup binder;

    // Easily bind forms to beans and manage validation and buffering
    private BeanFieldGroup<Freq> formFieldBindings;

    public FreqForm(FreqView freqView) {
    	this.freqView = freqView;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
    	jobTable = new JobTable();
    	
    	freqName.setValue("");
    	freqDesc.setValue("");
    	
    	initFreqTypes();
    	initInterval();
    	initRepeat();
    	
    	//startTime.setEnabled(false);
    	
    	executeOption.addItem("Execute now");
    	executeOption.addItem("Specific Time");
    	executeOption.select("Execute now");
    	executeOption.setValue("Execute now");
    	executeOption.addStyleName("horizontal");
    	executeOption.setMultiSelect(false);
    	/*executeOption.addValueChangeListener(event -> {
                if (executeOption.getValue().equals("Specific Time")) startTime.setEnabled(true); 
                else startTime.setEnabled(false);
        });*/
    	
    	executeOption.addValueChangeListener(event -> {
            if (executeOption.getValue().equals("Execute now")) {
            	//startTime = new TextField();
            	startTime.setVisible(false);
            } 
            else{
            	//startTime = new DateField();
            	startTime.setVisible(true);
            	
            }
    	});
    	
    	executeOption.setImmediate(true);
    	
    	//execute.addValueChangeListener(event -> startTime.setEnabled(execute.getValue()));
    	
    	configureStartTimeField(startTime);
    	
    	//startTime.setValue(new Date());
    	//startTime.setDateFormat("yyyy/MM/dd hh:mm:ss a");
    	//startTime.setShowISOWeekNumbers(true);
    	//date.setLenient(true);
    	//startTime.setResolution(Resolution.SECOND);
    	//startTime.setVisible(false);
    	
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        //save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
        
    }

    private void buildLayout() {
        setMargin(true);

        //HorizontalLayout actions = new HorizontalLayout(save, cancel);
        //actions.setSpacing(true);
        
        GridLayout toolbar = new GridLayout(2, 1);
		toolbar.setWidth("100%");
		
		toolbar.setSpacing(true);
		
		toolbar.addComponent(save, 0, 0);
		toolbar.addComponent(cancel, 1, 0);
		
		toolbar.setColumnExpandRatio(0, 3);
		
		toolbar.setComponentAlignment(save, Alignment.MIDDLE_RIGHT);
		toolbar.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);
        
        Label caption = new Label("Dispatch to job");
        caption.addStyleName("h3");
        caption.addStyleName("colored");
        
		addComponents(toolbar, freqName, freqDesc, freqType, executeOption, startTime, interval, repeat, caption, jobTable);
		
		setSizeFull();
		setSpacing(true);
		setHeight("100%");
		
    }

    public void save(Button.ClickEvent event) {
        // Commit the fields from UI to DAO
		saveFreq();
		saveJobs();
		
		String msg = String.format("Saved '%s %s'.",
				freqName.getValue(),
				types.get(freqType.getValue()));
		Notification.show(msg,Type.TRAY_NOTIFICATION);
		freqView.refreshJobs();
		addFlag = false;
		this.freq = null;
		freqView.getFreqList().select(null);
        freqView.getFreqPanel().setVisible(false);
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        
        freqView.getFreqList().select(null);
        freqView.getFreqPanel().setVisible(false);
        
        addFlag = false;
        this.freq = null;
    }

    public void add(Freq freq) {
    	freqView.getFreqPanel().setVisible(true);
    	freqView.getFreqPanel().setCaption("New Freq");
        this.freq = freq;
        addFlag = true;
        
        executeOption.select("Execute now");
        executeOption.setValue("Execute now");
        repeat.setValue(0);
        
        this.freq.setFreqType("s");
        //this.freq.setStartTime(null);
        
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("jobName", String.class, null);
        container.addContainerProperty("jobDesc", String.class, null);
        jobTable.update(container);
    	
        if(this.freq != null) {
            // Bind the properties of the contact POJO to fiels in this form
        	formFieldBindings = BeanFieldGroup.bindFieldsBuffered(this.freq, this);
        	
            //freqName.focus();
        }
        setVisible(this.freq != null);
    }
    
    public void edit(Object item){
		freqView.getFreqPanel().setVisible(true);
		Item freqItem = freqView.getFreq().getItem(item);
		
		if(freqItem != null){
			String freqid = (String) freqItem.getItemProperty("id").getValue();
			this.freq = freqView.getFreq().getItem(item).getEntity();
			freqView.getFreqPanel().setCaption("Freq - " + freqItem.getItemProperty("freqName").getValue());
			
			if(!EXECUTE_STR.equals(this.freq.getStartTime())){
			//if(this.freq.getStartTime() != null){
				executeOption.setValue("Specific Time");
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				try {
					startTime.setValue(df.parse(this.freq.getStartTime()));
					startTime.setDateFormat("yyyy/MM/dd hh:mm:ss a");
					//startTime.setValue(null);
				} catch (ReadOnlyException | ConversionException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				executeOption.setValue("Execute now");
			}
			
			jobTable.setOrgContainer(getJob(freqid));
			jobTable.update(getJob(freqid));

			//formFieldBindings = BeanFieldGroup.bindFieldsBuffered(this.freq, this);
			binder = new FieldGroup(freqItem);
			binder.setBuffered(true);

			binder.bind(freqName, "freqName");
			binder.bind(freqDesc, "freqDesc");
			binder.bind(freqType, "freqType");
			//binder.bind(startTime, "startTime");
			binder.bind(interval, "interval");
			binder.bind(repeat, "repeat");

			freqName.focus();
			
		}
		
		setVisible(freqItem != null);
    }
    
    public IndexedContainer getJob(String freqid){
    	IndexedContainer container = new IndexedContainer();
    	container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("jobName", String.class, null);
        container.addContainerProperty("jobDesc", String.class, null);
        
        EntityManager em = Persistence.createEntityManagerFactory("reportengine").createEntityManager();

        FreqService fService = new FreqService(em);
        
        Freq _freq = fService.findFreq(freqid);
        
        if(_freq != null){
        	orgJobList = _freq.getJobList();
        	
	        for(int i = 1; i< _freq.getJobList().size()+1; i++){
	        	Item item = container.addItem(i);
	        	item.getItemProperty("id").setValue(_freq.getJobList().get(i-1).getId());
	        	item.getItemProperty("jobName").setValue(_freq.getJobList().get(i-1).getJobName());
	        	item.getItemProperty("jobDesc").setValue(_freq.getJobList().get(i-1).getJobDesc());
	        }
        }
        
        return container;
    }
    
    public Freq getFreq(){
    	return freq;
    }
    
    public void initFreqTypes(){
    	types.put("s", "Secondly");
    	types.put("m", "Minutely");
    	types.put("h", "Hourly");
    	types.put("D", "Daily");
    	types.put("W", "Weekly");
    	types.put("M", "Monthly");
    	
    	freqType.setItemCaptionPropertyId("caption");
    	freqType.addContainerProperty("caption", String.class, "");
    	freqType.setNullSelectionAllowed(false);
    	
    	for (final String identifier : types.keySet()) {
    		freqType.addItem(identifier).getItemProperty("caption")
            .setValue(types.get(identifier));
    	}
    	
    	freqType.select("s");
    }
    
    
    public void initInterval(){
    	interval.setNullSelectionAllowed(false);
    	
    	for(int i = 1; i <= 60; i++){
    		interval.addItem(i);
    	}
    }
    
    public void initRepeat(){
    	repeat.setNullSelectionAllowed(false);
    	
    	for(int i = 0; i < 100; i++){
    		repeat.addItem(i);
    	}
    }
    
    public void bindField(Freq freq){
    	freq.setFreqName(freqName.getValue());
    	freq.setFreqDesc(freqDesc.getValue());
    	freq.setFreqType(freqType.getValue().toString());
    	freq.setInterval(Integer.parseInt(interval.getValue().toString()));
    	freq.setRepeat(Integer.parseInt(repeat.getValue().toString()));
    }
    
    public void saveFreq(){
    	DateFormat db = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	Timestamp ts;
    	
    	String sTime;
    	
    	if (executeOption.getValue().equals("Execute now")) {
    		sTime = EXECUTE_STR;
    	}else{
    		ts = new Timestamp(startTime.getValue().getTime());
    		sTime = ts.toString();
    	}
    	
    	if(addFlag){
    		bindField(this.freq);
    		
            freq.setStartTime(sTime);
    		
    		freqView.getFreq().addEntity(freq);
    		freqView.getFreq().commit();
    		
    	}
    	else{
    		updateFreqStartTime(sTime);
    		
    		try {
				binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void updateFreqStartTime(String sTime){
    	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("reportengine");
    	FreqService fService = new FreqService(em);
    	
        fService.updateFreqStartTime(this.freq.getId(), sTime);
    }
    
    public void saveJobs(){
    	List<Job> jList = getRevisedJobList();
    	
    	EntityManager em = Persistence.createEntityManagerFactory("reportengine").createEntityManager();
    	
    	FreqService fService = new FreqService(em);
        
    	if(addFlag){
    		fService.addJobList(freq.getId(), jList);
    	}else{
    		addList = new ArrayList<Job>();
    		delList = new ArrayList<Job>();
    		
    		for(Job j : orgJobList){
    		    if(!jList.contains(j)){
    		    	delList.add(j);
    		    }
        	}
        	
        	for(Job j : jList){
	        	if(!orgJobList.contains(j)){
	        		addList.add(j);
	        	}
        	}
        	
        	if(delList != null){
        		fService.removeJobList(freq.getId(), delList);
        	}
        	
        	if(addList != null){
        		fService.addJobList(freq.getId(), addList);
        	}
    	}
    }
    
    public List<Job> getRevisedJobListAdd(){
    	List<Job> fList = new ArrayList<>();
    	
    	for(int i = 0; i < jobTable.getRevisedContainer().size(); i++){
    		Item item = jobTable.getRevisedContainer().getItem(i+1);
    		Job job = new Job();
    		
    		job.setId((String)item.getItemProperty("id").getValue());
    		job.setJobName((String)item.getItemProperty("jobName").getValue());
    		job.setJobDesc((String)item.getItemProperty("jobDesc").getValue());
    		
    		fList.add(job);
    	}
    	
    	return fList;
    }
    
    public List<Job> getRevisedJobList(){
    	List<Job> fList = new ArrayList<>();
    	
    	Collection<Integer> tempList = (Collection<Integer>) jobTable.getRevisedContainer().getItemIds();
    	
    	for(Integer j : tempList){
    		Item item = jobTable.getRevisedContainer().getItem(j);
    		Job job = new Job();
    		
    		job.setId((String)item.getItemProperty("id").getValue());
    		job.setJobName((String)item.getItemProperty("jobName").getValue());
    		job.setJobDesc((String)item.getItemProperty("jobDesc").getValue());
    		
    		fList.add(job);
    	}
    	
    	return fList;
    }
    
    public void configureStartTimeField(DateField startTime){
        	startTime.setValue(new Date());
        	startTime.setDateFormat("yyyy/MM/dd hh:mm:ss a");
        	startTime.setShowISOWeekNumbers(true);
        	startTime.setResolution(Resolution.SECOND);
        	startTime.setVisible(false);
    }
}
