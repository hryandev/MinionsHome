package com.rex.core;

import com.rex.backend.Contact;
import com.rex.backend.ContactService;
import com.rex.core.forms.JobForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Title("Job")
public class JobView extends HorizontalLayout implements View{
	/*@WebServlet(urlPatterns = "/job/*")
    @VaadinServletConfiguration(ui = JobUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }*/
	
	private static final long serialVersionUID = 1L;
	TextField filter = new TextField();
    public Grid contactList = new Grid();
    Button newContact = new Button("New job");

    JobForm contactForm = new JobForm(this);
    
    public ContactService service = ContactService.createDemoService();
	
	public JobView(){
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
       newContact.addClickListener(e -> contactForm.edit(new Contact()));

       filter.setInputPrompt("Filter jobs...");
       filter.addTextChangeListener(e -> refreshContacts(e.getText()));

       contactList.setContainerDataSource(new BeanItemContainer<>(Contact.class));
       contactList.setColumnOrder("firstName", "lastName", "email");
       contactList.removeColumn("id");
       contactList.removeColumn("birthDate");
       contactList.removeColumn("phone");
       contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
       contactList.addSelectionListener(e
               -> contactForm.edit((Contact) contactList.getSelectedRow()));
       refreshContacts();
	}

	private void buildLayout() {
		HorizontalLayout actions = new HorizontalLayout(filter, newContact);
		actions.setWidth("100%");
		filter.setWidth("100%");
		actions.setExpandRatio(filter, 1);

		VerticalLayout left = new VerticalLayout(actions, contactList);
		left.setSizeFull();
		contactList.setSizeFull();
		left.setExpandRatio(contactList, 1);
		
		//HorizontalSplitPanel sp = new HorizontalSplitPanel();
		//sp.setSizeFull();
		
		HorizontalLayout mainLayout = new HorizontalLayout(left, contactForm);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(left, 1);
		
		//sp.setFirstComponent(left);
		//sp.setSecondComponent(contactForm);
		this.setHeight("100%");

		addComponents(mainLayout);
		
		
		// Split and allow resizing
		//setContent(mainLayout);
	}
	

	public void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    private void refreshContacts(String stringFilter) {
        contactList.setContainerDataSource(new BeanItemContainer<>(
                Contact.class, service.findAll(stringFilter)));
        contactForm.setVisible(false);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	

}

