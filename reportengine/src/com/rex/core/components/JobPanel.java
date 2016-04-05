package com.rex.core.components;

import com.rex.core.forms.JobForm;
import com.rex.core.forms.MailForm;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class JobPanel extends Panel{
	private static final long serialVersionUID = 5819965722042216078L;

	private JobForm jobForm;
	private MailForm mailForm;
	private TestForm testForm;
	private TabSheet tabs;
	
	Button save = new Button("Save", jobForm::save);
    Button cancel = new Button("Cancel", jobForm::cancel);

	public JobPanel(JobForm jobForm){
		super();
		this.jobForm = jobForm;
		mailForm = new MailForm();
		testForm = new TestForm();
		configureComponents();
		buildLayout();
	}
	
	private void configureComponents(){
		tabs = new TabSheet();
		
		Tab tabJob = tabs.addTab(jobForm, "Job Detail");
		Tab tabMail = tabs.addTab(mailForm, "Mail");
		//Tab tabTest = tabs.addTab(testForm, "Test");
		
		tabJob.setClosable(false);
		tabJob.setEnabled(true);
		
		tabMail.setClosable(false);
		tabMail.setEnabled(true);
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        //save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
	}
	
	private void buildLayout(){
		VerticalLayout content = new VerticalLayout();
		
		HorizontalLayout toolbar = new HorizontalLayout();
		
		toolbar.setSpacing(true);
		//toolbar.setWidth("100%");
		
		Label blank = new Label("");
		blank.setWidth("950px");
		
		//CssLayout toolbar = new CssLayout();
		//toolbar.setWidth("100%");
		
		toolbar.addComponents(blank, save, cancel);
		
		toolbar.setComponentAlignment(save, Alignment.BOTTOM_RIGHT);
		toolbar.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
		
		
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponents(toolbar, tabs);
		
		content.setResponsive(true);
		
		tabs.addStyleName("framed");
		
		tabs.setSizeFull();
		
		//content.setSizeFull();
		
		setWidth("100%");
		setVisible(false);
		setSizeFull();
		
		setContent(content);
		
	}
}
