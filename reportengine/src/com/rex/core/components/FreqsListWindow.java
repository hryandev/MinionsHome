package com.rex.core.components;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.core.ReportEngineUI;
import com.rex.core.forms.JobForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Ryan Hsu
 */

@SuppressWarnings("serial")
public class FreqsListWindow extends Window implements Button.ClickListener{

	private FreqTable freqTable;
	private Grid freqsList;
	
	private JPAContainer<Freq> freqs;
	IndexedContainer orgContainer;
    private Button saveButton;
    private Button cancelButton;
    
    //JobForm form;

    
    public FreqsListWindow() {
    	freqs = JPAContainerFactory.make(Freq.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
    	
    	configureComponents();
        buildLayout();
    }
    
    public FreqsListWindow(FreqTable freqTable) {
    	freqs = JPAContainerFactory.make(Freq.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
    	
    	this.freqTable = freqTable;
    	orgContainer = freqTable.getRevisedContainer();
    	configureComponents();
        buildLayout();
    }

    public FreqsListWindow(Job job, FreqTable freqTable) {
    	freqs = JPAContainerFactory.make(Freq.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
    	
    	this.freqTable = freqTable;
    	configureComponents();
        buildLayout();
    }
    
    
    public void configureComponents(){
    	freqsList = new Grid();
    	
    	IndexedContainer container = (IndexedContainer) freqTable.getFreqList().getContainerDataSource();
    	//List<String> filters = new ArrayList<>();
    	
    	//System.out.println("container="+container.size());
    	
    	for(int i = 0; i < orgContainer.size(); i++){
    		Item item = orgContainer.getItem(i+1);
    		Property<String> freqID = item.getItemProperty("id");
    		
    		//System.out.println("freqID="+freqID.getValue());
    		
    		Filter filter = new Not(new SimpleStringFilter("id", freqID.getValue(), true, false));
    		freqs.addContainerFilter(filter);
    	}
    	
    	freqsList.setContainerDataSource(freqs);
    	
    	freqsList.setColumnOrder("freqName", "freqDesc");
    	
    	freqsList.removeColumn("id");
        
    	freqsList.setSelectionMode(Grid.SelectionMode.MULTI);
    	
    }
    
    public void buildLayout(){
    	VerticalLayout root = new VerticalLayout();
    	
    	/*freqTable = new Table("Frequency", freqs);
    	freqTable.setSelectable(true);
    	freqTable.setImmediate(true);
    	freqTable.setVisibleColumns(new Object[] {"freqName", "freqDesc"});
    	freqTable.setColumnHeaders("Freq Name", "Description");
    	freqTable.setSelectable(true);*/
    	
    	HorizontalLayout toolbar = new HorizontalLayout();
    	saveButton = new Button("Add", this);
        cancelButton = new Button("Cancel", this);
        toolbar.addComponent(saveButton);
        toolbar.addComponent(cancelButton);
        toolbar.setSpacing(true);
    	
        root.addComponents(toolbar, freqsList);
        root.setExpandRatio(freqsList, 1);
        root.setSpacing(true);
        
        //setSizeUndefined();
        setContent(root);
        setCaption("Select Frequency");
    }

    /**
     * @return the caption of the editor window
     */
    /*private String buildCaption() {
        return String.format("%s %s", personItem.getItemProperty("firstName")
                .getValue(), personItem.getItemProperty("lastName").getValue());
    }*/

    @SuppressWarnings("unchecked")
	/*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {       	
        	//saveFreqs(getFreqList());
        	
        	freqTable.update(getFreqList());
            
        } else if (event.getButton() == cancelButton) {
            //editorForm.discard();
        }
        close();
    }

    public void addListener(EditorSavedListener editorSavedListener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { EditorSavedEvent.class });
            addListener(EditorSavedEvent.class, editorSavedListener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }

    public void removeListener(EditorSavedListener listener) {
        removeListener(EditorSavedEvent.class, listener);
    }
    
    

    public static class EditorSavedEvent<T> extends Component.Event {

        private Item savedItem;
        private Collection<T> freqs;

        /*public EditorSavedEvent(Component source, Collection<Object> collection) {
            super(source);
            this.savedItem = collection;
        }*/
        
        public EditorSavedEvent(Component source, Collection<T> freqs) {
            super(source);
            this.freqs = freqs;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }

    public interface EditorSavedListener extends Serializable {
        public void editorSaved(EditorSavedEvent event);
    }
    
    public IndexedContainer getFreqList(){
    	/*IndexedContainer container = new IndexedContainer();
    	container.addContainerProperty("id", String.class, null);
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);*/
    	
    	//List<Freq> selected = new ArrayList<Freq>();
    	
    	int j = orgContainer.size();
    	for(Object o : freqsList.getSelectedRows()){
    		Freq freq = freqs.getItem(o).getEntity();
    		Item item = orgContainer.addItem(j+1);
    		item.getItemProperty("id").setValue(freq.getId());
        	item.getItemProperty("freqName").setValue(freq.getFreqName());
        	item.getItemProperty("freqDesc").setValue(freq.getFreqDesc());
    		
        	j++;
    		//selected.add(freq);
    	}
    	
    	return orgContainer;
    	
    	//List<Object> lists = new ArrayList<Object>(freqsList.getSelectedRows());
    	//List<Freq> object = new ArrayList<Freq>();
    	//lists = (List) object;
    	//return (List<T>) lists;
    }
    
    // Iterate over the item identifiers of the table.
    /*for (Iterator i = table.getItemIds().iterator(); i.hasNext();) {
        // Get the current item identifier, which is an integer.
        int iid = (Integer) i.next();

        // Now get the actual item from the table.
        Item item = table.getItem(iid);

        // And now we can get to the actual checkbox object.
        Button button = (Button)
                (item.getItemProperty("ismember").getValue());

        // If the checkbox is selected.
        if ((Boolean)button.getValue() == true) {
            // Do something with the selected item; collect the
            // first names in a string.
            items += item.getItemProperty("First Name")
                         .getValue() + " ";
        }
    }*/
    

}
