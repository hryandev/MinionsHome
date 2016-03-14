package com.rex.core.forms;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.backend.service.JobService;
import com.rex.core.ReportEngineUI;
import com.rex.core.components.FreqTable;
import com.rex.core.components.FreqsListWindow;
import com.rex.core.views.FreqView;
import com.rex.core.views.JobView;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
    
    TextField freqMacro = new TextField("Description");
    CheckBox activate = new CheckBox("Activate");
    
    private static LinkedHashMap<String, String> types = new LinkedHashMap<String, String>();
    
    public FreqTable freqTable;
    public List<Freq> orgFreqs = new ArrayList<>();
    List<Freq> delList = null;
	List<Freq> addList = null;
    
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
    	freqTable = new FreqTable();
    	
    	freqName.setNullRepresentation("");
    	freqDesc.setNullRepresentation("");
    	freqMacro.setNullRepresentation("");
    	
    	initFreqTypes();
    	//freqType.setContainerDataSource();
    	freqType.setNullSelectionAllowed(false);
    	
    	freqType.setItemCaptionPropertyId("caption");
    	freqType.addContainerProperty("caption", String.class, "");
    	for (final String identifier : types.keySet()) {
    		freqType.addItem(identifier).getItemProperty("caption")
            .setValue(types.get(identifier));
    	}
    	
    	freqType.select("s");
    	
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
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
        
		addComponents(actions, freqName, freqDesc, freqType);
		
		setSizeFull();
		setSpacing(true);
		
    }

    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
        	if(addFlag){
        		formFieldBindings.commit();
        		freqView.getFreq().addEntity(freq);
        		freqView.getFreq().commit();
        	}
        	else{
        		
        		binder.commit();
        	}
        	
        	saveFreqs();
            
            String msg = String.format("Saved '%s %s'.",
            		freqName.getValue(),
            		freqMacro.getValue());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            freqView.refreshJobs();
            addFlag = false;
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        
        freqView.getFreqList().select(null);
        freqView.getJobPanel().setVisible(false);
    }

    public void add(Freq freq) {
    	freqView.getJobPanel().setVisible(true);
    	freqView.getJobPanel().setCaption("New Freq");
        this.freq = freq;
        addFlag = true;
        
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);
        
        freqTable.update(container);
    	
        if(freq != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(freq, this);
            freqName.focus();
        }
        setVisible(freq != null);
    }
    
    public void edit(Object item){
		freqView.getJobPanel().setVisible(true);
		Item freqItem = freqView.getFreq().getItem(item);
		
		if(freqItem != null){
			String freqid = (String)freqItem.getItemProperty("id").getValue();
			freq = freqView.getFreq().getItem(item).getEntity();
			freqView.getJobPanel().setCaption("Freq - " + freqItem.getItemProperty("freqName").getValue());
			freqTable.setOrgContainer(getFreqs(freqid));
			freqTable.update(getFreqs(freqid));
	
			if (freqItem != null) {
				// formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
				binder = new FieldGroup(freqItem);
				binder.setBuffered(true);
	
				binder.bind(freqName, "freqName");
				binder.bind(freqDesc, "freqDesc");
	
				freqName.focus();
			}
		}
		
		setVisible(freqItem != null);
    }
    
    /*public void openFreqsWindow(){
    	FreqsListWindow flw = new FreqsListWindow(job, freqTable);
    	getUI().addWindow(flw);
    	flw.center();
        flw.focus();
    }*/
    
    public IndexedContainer getFreqs(String freqid){
    	IndexedContainer container = new IndexedContainer();
    	container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);
        
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("reportengine");
        //EntityManager em = emf.createEntityManager();

        JobService jobService = new JobService();
        
        /*Freq _freq = jobService.findJob(freqid);
        
        if(_freq != null){
        	orgFreqs = _freq.getFreqs();
        	
	        for(int i = 1; i< _freq.getFreqs().size()+1; i++){
	        	Item item = container.addItem(i);
	        	item.getItemProperty("id").setValue(_freq.getFreqs().get(i-1).getId());
	        	item.getItemProperty("freqName").setValue(_freq.getFreqs().get(i-1).getFreqName());
	        	item.getItemProperty("freqDesc").setValue(_freq.getFreqs().get(i-1).getFreqDesc());
	        }
        }*/
        
        return container;
    }
    
    public Freq getFreq(){
    	return freq;
    }
    
    public void saveFreqs(){
    	List<Freq> fList = getRevisedFreqList();
    	
    	JobService jobService = new JobService();
        
    	if(addFlag){
    		jobService.addFreqs(freq.getId(), fList);
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
        		jobService.removeFreqs(freq.getId(), delList);
        	}
        	
        	if(addList != null){
        		jobService.addFreqs(freq.getId(), addList);
        	}
    	}
    }
    
    public List<Freq> getRevisedFreqList(){
    	List<Freq> fList = new ArrayList<>();
    	
    	for(int i = 0; i < freqTable.getRevisedContainer().size(); i++){
    		Item item = freqTable.getRevisedContainer().getItem(i+1);
    		Freq freq = new Freq();
    		
    		freq.setId((String)item.getItemProperty("id").getValue());
    		freq.setFreqName((String)item.getItemProperty("freqName").getValue());
    		freq.setFreqDesc((String)item.getItemProperty("freqDesc").getValue());
    		
    		fList.add(freq);
    	}
    	
    	return fList;
    	
    }
    
    public void initFreqTypes(){
    	types.put("s", "Every Second");
    	types.put("m", "Every Minute");
    	types.put("h", "Every Hour");
    	types.put("D", "Daily");
    	types.put("W", "Weekly");
    	types.put("M", "Monthly");
    	types.put("S", "Specific day");
    }

}
