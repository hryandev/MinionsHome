package com.rex.core.views;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.rex.backend.entity.Freq;
import com.rex.core.ReportEngineUI;
import com.rex.core.components.DateToStringConverter;
import com.rex.core.forms.FreqForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
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
@Title("Frequency")
public class FreqView extends HorizontalLayout implements View{
	private static final long serialVersionUID = -7993072104270238504L;
	
	public static final String NAME = "frequency";
	
	TextField filter = new TextField();
    public Grid freqList = new Grid();
    private final String FIRE_STR = "IMMEDIATELY";
    private String EXECUTE_STR = "";
    //public Table freqList = new Table();
    Button newContact = new Button();

    FreqForm freqForm;
    Panel rightPanel;
    
    private JPAContainer<Freq> freq;
    
	public FreqView(){
		freqForm = new FreqForm(this);
		rightPanel = new Panel();
		freq = JPAContainerFactory.make(Freq.class,
	               ReportEngineUI.PERSISTENCE_UNIT);
		
		configureComponents();
	    buildLayout();
	}
	
	private void configureComponents(){
       newContact.addClickListener(e -> freqForm.add(new Freq()));
       
       filter.setInputPrompt("Filter frequency...");
       filter.addTextChangeListener(e -> updateFilters(e.getText()));
       
       freqList.setContainerDataSource(freq);
       //initFireTimeString();
       
       freqList.getColumn("startTime").setConverter(new StringToDateConverter() {
    	   
    	   @Override
    	   public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale){
    		   if (targetType != getPresentationType()) {
    	            throw new ConversionException("Converter only supports " + getPresentationType().getName() + " (targetType was " + targetType.getName() + ")");
    	        }

    	        if (null == value){
    	        	return FIRE_STR;
    	        }
    			
    	        Timestamp ts = new Timestamp(value.getTime());
    	        
    	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(ts);
    	   }

       });
       
       freqList.setColumnOrder("freqName", "startTime");
       
       freqList.setImmediate(true);
       
       freqList.removeColumn("id");
       freqList.removeColumn("freqDesc");
       freqList.removeColumn("freqType");
       freqList.removeColumn("interval");
       freqList.removeColumn("repeat");
       freqList.removeColumn("jobList");
       
       
       freqList.setSelectionMode(Grid.SelectionMode.SINGLE);
       freqList.addSelectionListener(e -> freqForm.edit(freqList.getSelectedRow()));
       
       refreshJobs();

	}

	private void buildLayout() {
		HorizontalLayout actions = new HorizontalLayout(filter, newContact);
		
		newContact.setIcon(FontAwesome.PLUS);
		
		actions.setWidth("100%");
		filter.setWidth("100%");
		actions.setExpandRatio(filter, 1);

		VerticalLayout left = new VerticalLayout(actions, freqList);
		left.setSizeFull();
		freqList.setSizeFull();
		left.setExpandRatio(freqList, 1);
		
		VerticalLayout right = new VerticalLayout();
		right.addComponent(freqForm);
		right.setSizeFull();
		
		/*HorizontalLayout panelCaption = new HorizontalLayout();
		panelCaption.setMargin(true);
		panelCaption.setSpacing(true);
		panelCaption.addComponent(new Label("Test Panel Caption"));
		panelCaption.addComponent(new Button("Button1"));*/
		
		rightPanel.setWidth("100%");
		rightPanel.setContent(freqForm);
		rightPanel.setVisible(false);
		
		HorizontalSplitPanel sp = new HorizontalSplitPanel(left, rightPanel);
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

    
    private void updateFilters(String stringFilter){
        freq.setApplyFiltersImmediately(false);
        freq.removeAllContainerFilters();
        
        if (stringFilter != null && !stringFilter.equals("")) {
            Or or = new Or(new Like("freqName", stringFilter + "%", false),
                    new Like("startTime", stringFilter + "%", false));
            freq.addContainerFilter(or);
        }
        freq.applyFilters();
        
        freqForm.setVisible(false);
        rightPanel.setVisible(false);
    }
    
    public JPAContainer<Freq> getFreq(){
    	return freq;
    }
    
    public Panel getFreqPanel(){
    	return rightPanel;
    }
    
    public Grid getFreqList(){
    	return freqList;
    }
    
    public void initFireTimeString(){
    	for(Object o : freq.getItemIds()){
     	   Property<String> ft = freq.getContainerProperty(o, "startTime");
     	   if(EXECUTE_STR.equals(ft.getValue())){
     		  freq.getContainerProperty(o, "fireTime").setValue(FIRE_STR);
     	   }else{
     		  freq.getContainerProperty(o, "fireTime").setValue(ft.getValue()); 
     	   }
        }
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		freq.refresh();
		freqList.select(null);
		
		rightPanel.setVisible(false);
	}

}

