package com.rex.core.components;

import com.rex.components.valo.StringGenerator;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class TestForm extends FormLayout{

	public TestForm(){
		Label section = new Label("Personal Info");
        section.addStyleName("h2");
        section.addStyleName("colored");
        addComponent(section);
        StringGenerator sg = new StringGenerator();

        TextField name = new TextField("Name");
        name.setValue(sg.nextString(true) + " " + sg.nextString(true));
        name.setWidth("50%");
        addComponent(name);
        
        TextField email = new TextField("Email");
        email.setValue(sg.nextString(false) + "@" + sg.nextString(false)
                + ".com");
        email.setWidth("50%");
        email.setRequired(true);
        addComponent(email);
        
        setSpacing(true);
        setMargin(true);
        
        setMargin(false);
        addStyleName("light");
	}
	
	
}
