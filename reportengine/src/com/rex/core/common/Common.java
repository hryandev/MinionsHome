package com.rex.core.common;

import com.rex.backend.entity.Job;
import com.rex.backend.service.JobService;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class Common {
	public static IndexedContainer getFreqs(Job job){
    	IndexedContainer container = new IndexedContainer();
    	container.addContainerProperty("freqName", String.class, null);
        container.addContainerProperty("freqDesc", String.class, null);

        JobService jobService = new JobService();
        
        Job _job = jobService.findJob(job.getId());
        
        if(_job != null){
	        for(int i = 1; i< _job.getFreqs().size()+1; i++){
	        	Item item = container.addItem(i);
	        	item.getItemProperty("freqName").setValue(_job.getFreqs().get(i-1).getFreqName());
	        	item.getItemProperty("freqDesc").setValue(_job.getFreqs().get(i-1).getFreqDesc());
	        }
        }
        
        return container;
    }
}
