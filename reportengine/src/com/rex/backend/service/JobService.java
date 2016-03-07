package com.rex.backend.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

import com.rex.backend.Contact;
import com.rex.backend.ContactService;
import com.rex.backend.entity.Job;

public class JobService {

    // Create dummy data by randomly combining first and last names
    static String[] fnames = { "Peter", "Alice", "John", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene", "Lisa",
            "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
            "Jennifer" };
    static String[] lnames = { "Smith", "Johnson", "Williams", "Jones",
            "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Thompson", "Young", "King", "Robinson" };

    private static JobService instance;

    public static JobService createDemoService() {
        if (instance == null) {

            final JobService contactService = new JobService();

            Random r = new Random(0);
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 100; i++) {
            	Job contact = new Job();
                contact.setJob_Name(fnames[r.nextInt(fnames.length)]);
                contact.setJob_Desc(lnames[r.nextInt(fnames.length)]);
                contact.setJob_Macro(fnames[r.nextInt(fnames.length)]);
                contactService.save(contact);
            }
            instance = contactService;
        }

        return instance;
    }

    private HashMap<Long, Job> contacts = new HashMap<>();
    private long nextId = 0;

    /*public synchronized List<Contact> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Job contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ContactService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Job>() {

            @Override
            public int compare(Job o1, Job o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }*/

    public synchronized long count() {
        return contacts.size();
    }

    public synchronized void delete(Job value) {
        contacts.remove(value.getId());
    }

    public synchronized void save(Job entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            //entry = (Job) BeanUtils.cloneBean(entry);
        	
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        //contacts.put(entry.getId(), entry);
    }

}
