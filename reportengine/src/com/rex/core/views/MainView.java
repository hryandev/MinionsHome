package com.rex.core.views;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.rex.backend.entity.User;
import com.rex.components.valo.Icons;
import com.rex.components.valo.ValoMenuLayout;
import com.vaadin.annotations.Title;
import com.vaadin.event.UIEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;


/**
 * 
 * @author Ryan Hsu
 */


@Title("Main")
public class MainView extends ValoMenuLayout implements View{
	private static final long serialVersionUID = -6895300729283404531L;

	public static final String NAME = "main";
	
	public static final LinkedHashMap<String, View> viewMap = new LinkedHashMap<>();
	private Map<String, String> menuItems = new LinkedHashMap<String, String>();
	private Map<String, Resource> iconMap = new LinkedHashMap<>();
	
    private CssLayout menu = new CssLayout();
    private CssLayout menuItemsLayout = new CssLayout();
    private ReportView reportView = new ReportView();
    private TaskView taskView = new TaskView();
    
    private int reportCount = 0;
    private int taskCount = 0;
    
    private Resource photo = null;
    private final String photoRootPath = "\\\\163.50.48.12\\Photo\\";
    private final String DEFAULT_PHOTO = "../tests-valo/img/profile-pic-300px.jpg";
    
    private String userName = "Mr. Murata";
    private User user = null;
    
    private MenuBar settings;
    private MenuItem settingsItem;
    

	public MainView(){
		user = (User) UI.getCurrent().getSession().getAttribute("user");
		
		if(user != null){
			userName = user.getPrenom() + " " + user.getNom();
			String photoPath = photoRootPath + user.getId() + "-1.JPG";
			File file = new File(photoPath);
			photo = new FileResource(file);
		}else{
			photo = new ThemeResource(DEFAULT_PHOTO);
		}
		
		initViewMap();
		initComponents();
		addMenu(buildMenu());
		initNavigator();
		
		//reportView.initUserInfo(user);
		//reportView.initReport();
		//reportView.initTaskList();
		
	}
	
	public void initViewMap(){
		
		viewMap.put(ReportView.NAME, reportView);
		
		viewMap.put(JobView.NAME, new JobView());
		viewMap.put(FreqView.NAME, new FreqView());
		viewMap.put(TaskView.NAME, taskView);
	}
	
	public void initComponents(){
		setComponentAlignment(contentArea, Alignment.MIDDLE_LEFT);
	}
	
	public CssLayout buildMenu(){
		initMenuMap();

        final HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName("valo-menu-title");
        menu.addComponent(top);
        //menu.addComponent(createThemeSelect());

        final Button showMenu = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (menu.getStyleName().contains("valo-menu-visible")) {
                    menu.removeStyleName("valo-menu-visible");
                } else {
                    menu.addStyleName("valo-menu-visible");
                }
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName("valo-menu-toggle");
        showMenu.setIcon(FontAwesome.LIST);
        menu.addComponent(showMenu);

        final Label title = new Label(
                "<h3>Report <strong>EngineXcel</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);
        
        MenuBar.Command logOutCmd = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
            	// "Logout" the user
                getSession().setAttribute("user", null);
                getSession().close();

                // Refresh this view, should redirect to login view
                UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
                
            }
        };

        settings = new MenuBar();
        settings.addStyleName("user-menu");
        settingsItem = settings.addItem(userName,
        		photo,
                null);
        settingsItem.addItem("Edit Profile", null);
        settingsItem.addItem("Preferences", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Log Out", logOutCmd);
        
        menu.addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        Label label = null;
        int count = -1;
        taskCount = taskView.getTaskCount();
        for (final Entry<String, String> item : menuItems.entrySet()) {
            if (item.getKey().equals("job")) {
                label = new Label("Scheduler", ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("user")) {
            	label.setValue(label.getValue());
                /*label.setValue(label.getValue()
                        + " <span class=\"valo-menu-badge\">" + count
                        + "</span>");*/
                count = 0;
                label = new Label("Authorization", ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("kit1")) {
            	label.setValue(label.getValue());
                /*label.setValue(label.getValue()
                        + " <span class=\"valo-menu-badge\">" + count
                        + "</span>");*/
                count = 0;
                label = new Label("Kits", ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            final Button b = new Button(item.getValue(), new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                	
                	//getUI().getNavigator().navigateTo(item.getKey());
                	// Navigate to a specific state
                	getUI().getNavigator().navigateTo(NAME + "/" + item.getKey());
                }
            });
            
            if("report".equals(item.getKey())) {
                b.setCaption(b.getCaption()
                        + " <span class=\"valo-menu-badge\">"+reportCount+"</span>");
            }else if("task".equals(item.getKey())) {
                b.setCaption(b.getCaption()
                        + " <span class=\"valo-menu-badge\">"+taskCount+"</span>");
            }
            
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName("valo-menu-item");
            
            b.setIcon(new Icons(item.getKey()).get());
            
            b.setIcon((Resource)iconMap.get(item.getKey()));
            
            menuItemsLayout.addComponent(b);
            count++;
        }
        
        label.setValue(label.getValue());
        
        //label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">"
        //        + count + "</span>");

        return menu;
    
    }
	
	public void initNavigator(){
		UI.getCurrent().getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
            	// Check if a user has logged in
                boolean isLoggedIn = UI.getCurrent().getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;
                

                if(!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
                    return false;

                } else if(isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }
                
                return true;
            
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
            	/*boolean isLoggedIn = UI.getCurrent().getSession().getAttribute("user") != null;
            	String f = Page.getCurrent().getUriFragment();
            	
            	if(isLoggedIn && f != null && "".equals(f)){
                	UI.getCurrent().getNavigator().navigateTo(MainView.NAME);
                }*/
            	
            	
            	if (UI.getCurrent().getPage().getWebBrowser().isIE()
                        && UI.getCurrent().getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
                    menu.setWidth("320px");
                }
            	
                for (final Iterator<Component> it = menuItemsLayout.iterator(); it
                        .hasNext();) {
                    it.next().removeStyleName("selected");
                }
                for (final Entry<String, String> item : menuItems.entrySet()) {
                	
                    if (event.getParameters().equals(item.getKey())) {
                        for (final Iterator<Component> it = menuItemsLayout
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
                menu.removeStyleName("valo-menu-visible");
                
                
                //String f = Page.getCurrent().getUriFragment();
                
                //if(null != f && !"".equals(f)){
                	//f = f.substring(5);
                	
	                if("task".equals(event.getParameters())){
	                	//taskCount = taskView.getTaskCount();
	                	//Button taskBtn = (Button)menuItemsLayout.getComponent(3);
	                	//String caption = "Task <span class=\"valo-menu-badge\">"+taskCount+"</span>";
	                	//taskBtn.setCaption(caption);
	                	//menuItemsLayout.getComponent(3).setCaption(caption);
	                	
	                	refreshTaskCaption();
	                	
	                	UI.getCurrent().setPollInterval(50000);
	                	UI.getCurrent().addPollListener(new UIEvents.PollListener() {
	                        @Override
	                        public void poll(UIEvents.PollEvent event) {
	                            //log.debug("Polling");
	                        	
	                        	Page.getCurrent().reload();
	                        	
	                        	//taskBtn.setCaption(caption);
	                        	//menuItemsLayout.getComponent(3).setCaption(caption);
	                        	
	                        	refreshTaskCaption();
	                        }
	                    });
	                }else if("report".equals(event.getParameters())){
	                	refreshReportCaption();
	                	
	                	UI.getCurrent().setPollInterval(50000);
	                	UI.getCurrent().addPollListener(new UIEvents.PollListener() {
	                        @Override
	                        public void poll(UIEvents.PollEvent event) {
	                            //log.debug("Polling");
	                        	
	                        	Page.getCurrent().reload();
	                        	
	                        	refreshReportCaption();
	                        }
	                    });
	                	
	                }else{
	                	UI.getCurrent().setPollInterval(-1);
	                }
                //}
                
            }
        });
	}
	
	public void initMenuMap(){
		
		menuItems.put("report", "Report");
        menuItems.put("job", "Job");
        menuItems.put("task", "Task");
        menuItems.put("frequency", "Frequency");
        menuItems.put("user", "User");
        menuItems.put("role", "Role");
        menuItems.put("group", "Group");
        //menuItems.put("kit1", "Kit1");
        //menuItems.put("kit2", "Kit2");
        //menuItems.put("kit3", "Kit3");
		
		iconMap.put("report", FontAwesome.BAR_CHART);
		iconMap.put("job", FontAwesome.CALENDAR_CHECK_O);
		iconMap.put("task", FontAwesome.TASKS);
		iconMap.put("frequency", FontAwesome.CLOCK_O);
		iconMap.put("user", FontAwesome.USER);
		iconMap.put("role", FontAwesome.USER_SECRET);
		iconMap.put("group", FontAwesome.GROUP);
		//iconMap.put("kit1", new Icons("kit1").get());
		//iconMap.put("kit2", new Icons("kit2").get());
		//iconMap.put("kit3", new Icons("kit3").get());
		
	}
	
	public void refreshReportCaption(){
		reportCount = reportView.getReports();
    	String caption = "Report <span class=\"valo-menu-badge\">"+reportCount+"</span>";
    	menuItemsLayout.getComponent(0).setCaption(caption);
	}
	
	public void refreshTaskCaption(){
		taskCount = taskView.getTaskCount();
		String caption = "Task <span class=\"valo-menu-badge\">"+taskCount+"</span>";
		menuItemsLayout.getComponent(3).setCaption(caption);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		user = (User) UI.getCurrent().getSession().getAttribute("user");
		boolean isLoggedIn = user != null;
		
		if(isLoggedIn && "".equals(event.getParameters())){
			String photoPath = photoRootPath + user.getId() + "-1.JPG";
			File file = new File(photoPath);
			
			settingsItem.setText(user.getPrenom() + " " + user.getNom());
			
			if(file.exists()){
				settingsItem.setIcon(new FileResource(file));
			}else{
				settingsItem.setIcon(new ThemeResource(DEFAULT_PHOTO));
			}
			
		}
		
		if(isLoggedIn){
			reportView.initUserInfo(user);
			
			if(!reportView.isFlag()){
				reportView.initReport();
				reportView.initTaskList();
			}
			
			refreshReportCaption();
		}

		if (event.getParameters() == null || event.getParameters().isEmpty()) {
			//contentArea.addComponent(null);
			return;
		} else {
			if(contentArea.getComponentCount() != 0){
				contentArea.removeAllComponents();
			}
			
			String viewName = event.getParameters();
			
			contentArea.addComponent((Component) viewMap.get(viewName));
			viewMap.get(viewName).enter(event);
		}

	}

}

