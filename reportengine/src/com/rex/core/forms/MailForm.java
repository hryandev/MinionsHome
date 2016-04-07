package com.rex.core.forms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.rex.backend.entity.Job;
import com.rex.backend.entity.User;
import com.rex.backend.entity.UserGroup;
import com.rex.backend.service.JobService;
import com.rex.core.ReportEngineUI;
import com.rex.core.components.AddressEditor;
import com.rex.core.views.JobView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MailForm extends VerticalLayout{
	private static final long serialVersionUID = 3800845557046000953L;

	private FormLayout form;
	
	private AddressEditor to;
	private AddressEditor cc;
	private RichTextArea mailBody;
	private TextField mailSubject;
	
	public static JPAContainer<User> users = null;
	List<String> result = null;
	
	public List<String> orgToAddr = new ArrayList<>();
	public List<String> orgCcAddr = new ArrayList<>();
	
	private Job job;
	private JobView jobView;
	public boolean addFlag = false;
	
	private List<String> addList = null;
	private List<String> delList = null;
	
	public FieldGroup binder;
	public BeanFieldGroup<Job> formFieldBindings;
	
	public MailForm(){
		configureComponents();
		buildLayout();
	}
	
	public MailForm(JobView jobView) {
    	this.jobView = jobView;
        configureComponents();
        buildLayout();
        
        initAddrList();
    }
	
	public MailForm(JobView jobView, Job job) {
    	this.jobView = jobView;
    	this.job = job;
        configureComponents();
        buildLayout();
        
        initAddrList();
    }
	
	private void configureComponents(){
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		
		//this.setHeight("100%");
		
		final Label caption = new Label("To send report to users");
		caption.addStyleName("h3");
		caption.addStyleName("colored");
		addComponent(caption);
		
		this.setCaption("Mail");
		//addStyleName(ValoTheme.LAYOUT_CARD);
		
		final CssLayout wrap = new CssLayout();
		wrap.setWidth("600px");
		wrap.addStyleName(ValoTheme.LAYOUT_CARD);
		addComponent(wrap);
		setExpandRatio(wrap, 1);
		
		form = new FormLayout();
		form.setMargin(false);
		form.setSpacing(true);
		form.setWidth("100%");
		form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		
		wrap.addComponent(form);
		
		to = new AddressEditor();
		cc = new AddressEditor();
		mailBody = new RichTextArea();
		mailSubject = new TextField("Subject");
		mailBody.setNullRepresentation("");
		mailSubject.setNullRepresentation("");
		
		to.setReadOnly(true);
		cc.setReadOnly(true);
		
		form.addComponent(to);
		form.addComponent(cc);
		form.addComponent(mailSubject);
		
		wrap.addComponent(mailBody);
		
	}
	
	private void buildLayout(){
		to.setCaption("To");
		to.setWidth("100%");
		
		cc.setCaption("Cc");
		cc.setWidth("100%");
		
		mailBody.setWidth("100%");
		
		mailSubject.setWidth("100%");
	}
	
	public List<String> buildDemoAddresses() {
		/* Create dummy data by randomly combining first and last names */
		String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
				"Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
				"Lisa", "Marge" };
		String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
				"Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
				"Barks", "Ross", "Schneider", "Tate" };
		
		List<String> result = new LinkedList<String>();
		for (int i = 0; i < 1000; i++) {
			String fname = fnames[(int) (fnames.length * Math.random())].toLowerCase();
			String lname = lnames[(int) (lnames.length * Math.random())].toLowerCase(); 
			String address = fname + "." + lname + "@demo.com";
			result.add(address);
		}

		return result;
	}

	public void buildAddresses() {
		if (null == users) {
			users = JPAContainerFactory.make(User.class, ReportEngineUI.PERSISTENCE_UNIT);
		}
		result = new LinkedList<String>();

		for (Object item : users.getItemIds()) {
			Item userItem = users.getItem(item);
			String address = (String) userItem.getItemProperty("mail").getValue();
			result.add(address);
		}

		to.setAddresses(result);
		cc.setAddresses(result);

		return;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public RichTextArea getMailBody() {
		return mailBody;
	}

	public void setMailBody(RichTextArea mailBody) {
		this.mailBody = mailBody;
	}

	public TextField getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(TextField mailSubject) {
		this.mailSubject = mailSubject;
	}

	public AddressEditor getTo() {
		return to;
	}

	public void setTo(AddressEditor to) {
		this.to = to;
	}

	public AddressEditor getCc() {
		return cc;
	}

	public void setCc(AddressEditor cc) {
		this.cc = cc;
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
            
            addFlag = false;
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }
	
	public void saveUserGroup(boolean addFlag, String tocc) {
		List<String> orgAddrList = new ArrayList<>();
		List<String> addrList = new ArrayList<>();
		
		if("T".equals(tocc)){
			addrList = to.getValue();
			orgAddrList = orgToAddr;
		}else{
			addrList = cc.getValue();
			orgAddrList = orgCcAddr;
		}
		
		JobService jobService = new JobService();

		if (addFlag) {
			
			jobService.addUserGroup(job.getId(), addrList, tocc);
		} else {
			addList = new ArrayList<String>();
			delList = new ArrayList<String>();

			for (String addr : orgAddrList) {
				if (!addrList.contains(addr)) {
					delList.add(addr);
				}
			}

			for (String addr : addrList) {
				if (!orgAddrList.contains(addr)) {
					addList.add(addr);
				}
			}

			if (delList != null) {
				jobService.removeUserGroup(job.getId(), delList);
			}

			if (addList != null) {
				jobService.addUserGroup(job.getId(), addList, tocc);
			}
		}
	}
	
	public void initAddrList(){
		JobService jobService = new JobService();
		
		if (job != null) {
			List<UserGroup> usersList = jobService.findUsersByGroup(job.getId());

			if (usersList != null) {
				for (UserGroup user : usersList) {
					if ("T".equals(user.getTocc())) {
						orgToAddr.add(user.getUserID());
					} else {
						orgCcAddr.add(user.getUserID());
					}
				}

				to.setValue(orgToAddr);
				cc.setValue(orgCcAddr);
			}
		}
		
	}
	
	public void add(Job job) {
        this.job = job;
        
        if(job != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(job, this);
            
        }
        setVisible(job != null);
    }
	
	public void edit(Object item) {
		Item jobItem = jobView.getJob().getItem(item);

		if (jobItem != null) {
			binder = new FieldGroup(jobItem);
			binder.setBuffered(true);

			binder.bind(mailSubject, "mailSubject");
			binder.bind(mailBody, "mailBody");

		}

		setVisible(jobItem != null);
	}
}
