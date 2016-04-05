package com.rex.core.forms;

import java.util.LinkedList;
import java.util.List;

import com.rex.core.components.AddressEditor;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
public class MailForm extends VerticalLayout{
	private static final long serialVersionUID = 3800845557046000953L;

	private FormLayout form;
	
	private AddressEditor to;
	private AddressEditor cc;
	private RichTextArea body;
	private TextField subject;
	
	public MailForm(){
		configureComponents();
		buildLayout();
	}
	
	private void configureComponents(){
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		
		//this.setHeight("100%");
		
		final Label caption = new Label("Mail");
		addComponent(caption);
		
		//setWidth("600px");
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
		body = new RichTextArea();
		subject = new TextField("Subject");
		
		to.setReadOnly(true);
		cc.setReadOnly(true);
		
		to.setAddresses(buildDemoAddresses());
		cc.setAddresses(buildDemoAddresses());
		
		form.addComponent(to);
		form.addComponent(cc);
		form.addComponent(subject);
		
		wrap.addComponent(body);
		
	}
	
	private void buildLayout(){
		to.setCaption("To");
		to.setWidth("100%");
		
		cc.setCaption("Cc");
		cc.setWidth("100%");
		
		body.setWidth("100%");
		
		subject.setWidth("100%");
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
	
}
