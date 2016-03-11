/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rex.core.components;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.backend.service.JobService;
import com.rex.core.ReportEngineUI;
import com.rex.core.common.Common;
import com.rex.core.forms.JobForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FreqsListWindow extends Window implements Button.ClickListener{

	private FreqTable freqTable;
	private Grid freqsList;
	private JPAContainer<Freq> freqs;
	//private final Item personItem;
    private Button saveButton;
    private Button cancelButton;
    
    RefreshableBeanItemContainer<Freq> freqContainer;
    JobForm form;
    
    private Job job;

    
    public FreqsListWindow() {
    	freqs = JPAContainerFactory.make(Freq.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
    	
    	configureComponents();
        buildLayout();
    }

    public FreqsListWindow(Job job, FreqTable freqTable) {
    	freqs = JPAContainerFactory.make(Freq.class,
    			ReportEngineUI.PERSISTENCE_UNIT);
    	
    	this.freqTable = freqTable;
        this.job = job;
    	configureComponents();
        buildLayout();
    }
    
    
    public void configureComponents(){
    	freqsList = new Grid();
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
    	
        root.addComponents(toolbar, freqsList);
        root.setExpandRatio(freqsList, 1);
        
        setSizeUndefined();
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
        	//job.setFreqs(new HashSet(freqsList.getSelectedRows()));
        	//List<Freq> freqs = getFreqList();
        	saveFreqs(getFreqList());
        	freqTable.update(Common.getFreqs(job));
            
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
    
    public void saveFreqs(List<Freq> freqs){
        JobService jobService = new JobService();
        jobService.updateFreqs(job.getId(), freqs);
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
    
    public List<Freq> getFreqList(){
    	List<Freq> selected = new ArrayList<Freq>();
    	
    	for(Object o : freqsList.getSelectedRows()){
    		Freq freq = freqs.getItem(o).getEntity();
    		selected.add(freq);
    	}
    	
    	return selected;
    	
    	//List<Object> lists = new ArrayList<Object>(freqsList.getSelectedRows());
    	//List<Freq> object = new ArrayList<Freq>();
    	//lists = (List) object;
    	//return (List<T>) lists;
    }

}
