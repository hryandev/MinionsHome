package com.rex.core.views;

import java.util.Iterator;
import java.util.Map.Entry;

import com.rex.backend.entity.User;
import com.rex.backend.service.UserService;
import com.rex.core.ReportEngineUI;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.event.UIEvents;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class SimpleLoginView extends CustomComponent implements View,
        Button.ClickListener {
	private static final long serialVersionUID = 2607449182760444787L;

	public static final String NAME = "login";

    private final TextField user;
    private final PasswordField password;
    private final Button loginButton;
    
    //private LoginForm loginForm = new LoginForm();
    
    public static Navigator navigator;
    

    public SimpleLoginView() {
        setSizeFull();

        // Create the user input field
        user = new TextField("User:");
        user.setWidth("300px");
        user.setRequired(true);
        user.setInputPrompt("Your Staff ID");
        //user.addValidator(new EmailValidator(
        //        "Username must be an email address"));
        //user.setInvalidAllowed(false);

        // Create the password input field
        password = new PasswordField("Password:");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        // Create login button
        loginButton = new Button("Login", this);

        // Add both to a panel
        VerticalLayout fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // focus the username field when user arrives to the login view
        user.focus();
    }

    // Validator for validating the passwords
    private static final class PasswordValidator extends
            AbstractValidator<String> {

        public PasswordValidator() {
            super("The password provided is not valid");
        }

        @Override
        protected boolean isValidValue(String value) {
            //
            // Password must be at least 8 characters long and contain at least
            // one number
            //
            if (value != null
                    && (value.length() < 5 || !value.matches(".*\\d.*"))) {
                return false;
            }
            return true;
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {

        //
        // Validate the fields using the navigator. By using validors for the
        // fields we reduce the amount of queries we have to use to the database
        // for wrongly entered passwords
        //
        if (!user.isValid() || !password.isValid()) {
            return;
        }

        String username = user.getValue();
        String password = this.password.getValue();

        //
        // Validate username and password with database here. For examples sake
        // I use a dummy username and password.
        //
        
        UserService uService = new UserService();
        User userBean = uService.findUser(username);
        
        boolean isValid = false;
        
        if(null == userBean){
        	String error = "Username is invalid.";
        	
        	Notification notification = new Notification("Error", error,
                    Type.ERROR_MESSAGE, true);

            notification.show(Page.getCurrent());
        }else{
        	isValid = password.equals(userBean.getPwd());
        }
        
       // boolean isValid = username.equals("test@test.com")
       //         && password.equals("passw0rd");

        if (isValid) {

            // Store the current user in the service session
            getSession().setAttribute("user", username);
            
            ReportEngineUI.configAfterSuccess();
            
            // Navigate to main view
            //getUI().getNavigator().navigateTo(MainView.NAME);

        } else {

            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            this.password.focus();

        }
    }
    
    public void configAfterSuccess(){
    	ReportEngineUI.navigator = new Navigator(getUI(), ReportEngineUI.viewDisplay);
    	
        ReportEngineUI.root.addMenu(ReportEngineUI.buildMenu());
        getUI().setContent(ReportEngineUI.root);
        
        ReportEngineUI.navigator.addView(SimpleLoginView.NAME, SimpleLoginView.class);
        ReportEngineUI.navigator.addView(MainView.NAME, MainView.class);
        ReportEngineUI.navigator.addView("reportlist", MainView.class);
        ReportEngineUI.navigator.addView("job", new JobView());
        ReportEngineUI.navigator.addView("task", ReportEngineUI.taskView);
        ReportEngineUI.navigator.addView("frequency", new FreqView());
        
        
        final String f = Page.getCurrent().getUriFragment();
        if (f == null || f.equals("")) {
            //navigator.navigateTo(MainView.NAME);
            navigator.navigateTo(SimpleLoginView.NAME);
        }

        navigator.setErrorView(MainView.class);
        
        ReportEngineUI.navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {           	            	
                for (final Iterator<Component> it = ReportEngineUI.menuItemsLayout.iterator(); it
                        .hasNext();) {
                    it.next().removeStyleName("selected");
                }
                for (final Entry<String, String> item : ReportEngineUI.menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (final Iterator<Component> it = ReportEngineUI.menuItemsLayout
                                .iterator(); it.hasNext();) {
                            final Component c = it.next();
                            if (c.getCaption() != null
                                    && c.getCaption().startsWith(
                                            item.getValue())) {
                                c.addStyleName("selected");
                                break;
                            }
                        }
                        break;
                    }
                }
                ReportEngineUI.menu.removeStyleName("valo-menu-visible");
                
                
                final String f = Page.getCurrent().getUriFragment();
                if(f.equals("!task")){
                	ReportEngineUI.taskCount = ReportEngineUI.taskView.getTaskCount();
                	
                	Button taskBtn = (Button)ReportEngineUI.menuItemsLayout.getComponent(3);
                	
                	String caption = "Task <span class=\"valo-menu-badge\">"+ReportEngineUI.taskCount+"</span>";
                	
                	taskBtn.setCaption(caption);
                	
                	UI.getCurrent().setPollInterval(50000);
                	UI.getCurrent().addPollListener(new UIEvents.PollListener() {
                        @Override
                        public void poll(UIEvents.PollEvent event) {
                            //log.debug("Polling");
                        	
                        	Page.getCurrent().reload();
                        	
                        	taskBtn.setCaption(caption);
                        }
                    });
                }else{
                	UI.getCurrent().setPollInterval(-1);
                }
                
            }
        });
    }
    
    
}