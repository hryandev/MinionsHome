package com.rex.core.views;

import com.rex.backend.entity.User;
import com.rex.backend.service.UserService;
import com.rex.components.valo.Icons;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LoginView extends VerticalLayout implements View,
        Button.ClickListener {
	private static final long serialVersionUID = 2607449182760444787L;

	public static final String NAME = "";

    private final TextField user;
    private final PasswordField password;
    private final Button loginButton;
    

    public LoginView() {
        setSizeFull();

        final CssLayout wrap = new CssLayout();
		wrap.setWidth("420px");
		
        // Create the user input field
        user = new TextField("User");
        user.setWidth("300px");
        user.setRequired(true);
        user.setInputPrompt("staff ID");        
        user.setIcon(FontAwesome.USER);
        //user.addValidator(new EmailValidator(
        //        "Username must be an email address"));
        //user.setInvalidAllowed(false);

        // Create the password input field
        password = new PasswordField("Password");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");
        password.setIcon(FontAwesome.LOCK);
        
        user.addStyleName("inline-icon");
        password.addStyleName("inline-icon");

        // Create login button
        loginButton = new Button("Login", this);
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        
        Resource res = new Icons("kanban").get();
        Image image = new Image(null, res);
        
        // Add both to a panel
        VerticalLayout fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("");
        fields.setSpacing(true);
        fields.setSizeFull();
        
        FormLayout form = new FormLayout(fields);
        form.setMargin(new MarginInfo(true, true, true, true));
        form.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        
        wrap.addComponent(form);
        wrap.addStyleName(ValoTheme.LAYOUT_CARD);
        
        VerticalLayout kanban = new VerticalLayout();
        kanban.setWidth("420px");
        kanban.addComponents(image, wrap);
        
        addComponent(kanban);
        setComponentAlignment(kanban, Alignment.MIDDLE_CENTER);
        
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

        if (isValid) {

            // Store the current user in the service session
            getSession().setAttribute("user", userBean);
            
            //ReportEngineUI.configAfterSuccess();
            
            // Navigate to main view
            getUI().getNavigator().navigateTo(MainView.NAME);

        } else {

            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            this.password.focus();

        }
    }
    
    
}