package com.rex.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.rex.components.valo.ComboBoxes;
import com.rex.components.valo.CommonParts;
import com.rex.components.valo.Icons;
import com.rex.components.valo.StringGenerator;
import com.rex.components.valo.TestIcon;
import com.rex.components.valo.ValoMenuLayout;
import com.rex.components.valo.ValoThemeSessionInitListener;
import com.rex.core.views.FreqView;
import com.rex.core.views.JobView;
import com.rex.core.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;


/**
 * 
 * @author Ryan Hsu
 */

@SuppressWarnings("serial")
@Theme("tests-valo")
@Title("Report EngineXcel")
public class ReportEngineUI extends UI {
	public static final String PERSISTENCE_UNIT = "reportengine";
	
    private boolean testMode = false;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ReportEngineUI.class)
    public static class Servlet extends VaadinServlet {

        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addSessionInitListener(
                    new ValoThemeSessionInitListener());
        }
    }
    
    private static LinkedHashMap<String, String> themeVariants = new LinkedHashMap<String, String>();
    
    static {
    	//DataGenerator.create();
    	
        themeVariants.put("tests-valo", "Default");
        themeVariants.put("tests-valo-blueprint", "Blueprint");
        themeVariants.put("tests-valo-dark", "Dark");
        themeVariants.put("tests-valo-facebook", "Facebook");
        themeVariants.put("tests-valo-flatdark", "Flat dark");
        themeVariants.put("tests-valo-flat", "Flat");
        themeVariants.put("tests-valo-light", "Light");
        themeVariants.put("tests-valo-metro", "Metro");
        themeVariants.put("tests-valo-reindeer", "Migrate Reindeer");
    }
    
    //private final TestIcon testIcon = new TestIcon(100);

    ValoMenuLayout root = new ValoMenuLayout();
    ComponentContainer viewDisplay = root.getContentContainer();
    CssLayout menu = new CssLayout();
    CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("testMenu");
    }
    private Navigator navigator;
    private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();
    
    @Override
    protected void init(final VaadinRequest request) {
        if (request.getParameter("test") != null) {
            testMode = true;

            if (browserCantRenderFontsConsistently()) {
                getPage().getStyles().add(
                        ".v-app.v-app.v-app {font-family: Sans-Serif;}");
            }
        }

        if (getPage().getWebBrowser().isIE()
                && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }
        // Show .v-app-loading valo-menu-badge
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        if (!testMode) {
            Responsive.makeResponsive(this);
        }

        getPage().setTitle("Report EngineXcel");
        setContent(root);
        setTheme("tests-valo-light");
        
        root.setComponentAlignment(viewDisplay, Alignment.MIDDLE_LEFT);
        
        root.addMenu(buildMenu());
        addStyleName(ValoTheme.UI_WITH_MENU);
        
        navigator = new Navigator(this, viewDisplay);

        navigator.addView("reportlist", MainView.class);
        navigator.addView("job", JobView.class);
        //navigator.addView("task", ButtonsAndLinks.class);
        navigator.addView("frequency", FreqView.class);
        //navigator.addView("user", DateFields.class);
        navigator.addView("kit2", ComboBoxes.class);
        //navigator.addView("checkboxes", CheckBoxes.class);
        //navigator.addView("sliders", Sliders.class);
        //navigator.addView("group", MenuBars.class);
        //navigator.addView("panels", Panels.class);
        //navigator.addView("trees", Trees.class);
        //navigator.addView("tables", Tables.class);
        //navigator.addView("spanels", SplitPanels.class);
        //navigator.addView("kit1", Tabsheets.class);
        //navigator.addView("kit2", Tables.class);
        //navigator.addView("colorpickers", ColorPickers.class);
        //navigator.addView("selects", NativeSelects.class);
        //navigator.addView("kit3", JobView.class);
        //navigator.addView("popupviews", PopupViews.class);
        //navigator.addView("dragging", Dragging.class);

        final String f = Page.getCurrent().getUriFragment();
        if (f == null || f.equals("")) {
            navigator.navigateTo("reportlist");
        }

        navigator.setErrorView(CommonParts.class);

        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                for (final Iterator<Component> it = menuItemsLayout.iterator(); it
                        .hasNext();) {
                    it.next().removeStyleName("selected");
                }
                for (final Entry<String, String> item : menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
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
            }
        });

    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        // IE8 also has randomness in its font rendering...

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS")
                || (getPage().getWebBrowser().isIE() && getPage()
                        .getWebBrowser().getBrowserMajorVersion() <= 9);
    }

    public static boolean isTestMode() {
        return ((ReportEngineUI) getCurrent()).testMode;
    }

    Component buildTestMenu() {
        final CssLayout menu = new CssLayout();
        menu.addStyleName("large-icons");

        final Label logo = new Label("Va");
        logo.setSizeUndefined();
        logo.setPrimaryStyleName("valo-menu-logo");
        menu.addComponent(logo);

        Button b = new Button(
                "Reference <span class=\"valo-menu-badge\">3</span>");
        b.setIcon(FontAwesome.TH_LIST);
        b.setPrimaryStyleName("valo-menu-item");
        b.addStyleName("selected");
        b.setHtmlContentAllowed(true);
        menu.addComponent(b);

        b = new Button("API");
        b.setIcon(FontAwesome.BOOK);
        b.setPrimaryStyleName("valo-menu-item");
        menu.addComponent(b);

        b = new Button("Examples <span class=\"valo-menu-badge\">12</span>");
        b.setIcon(FontAwesome.TABLE);
        b.setPrimaryStyleName("valo-menu-item");
        b.setHtmlContentAllowed(true);
        menu.addComponent(b);

        return menu;
    }

    CssLayout buildMenu() {
        // Add items
        menuItems.put("reportlist", "Report List");
        menuItems.put("job", "Job");
        menuItems.put("task", "Task");
        menuItems.put("frequency", "Frequency");
        menuItems.put("user", "User");
        //menuItems.put("comboboxes", "Combo Boxes");
        //menuItems.put("selects", "Selects");
        //menuItems.put("checkboxes", "Check Boxes & Option Groups");
        //menuItems.put("sliders", "Sliders & Progress Bars");
        //menuItems.put("colorpickers", "Color Pickers");
        menuItems.put("group", "Group");
        //menuItems.put("trees", "Trees");
        //menuItems.put("tables", "Tables & Grids");
        //menuItems.put("dragging", "Drag and Drop");
        menuItems.put("kit1", "Kit1");
        menuItems.put("kit2", "Kit2");
        menuItems.put("kit3", "Kit3");
        //menuItems.put("accordions", "Accordions");
        //menuItems.put("popupviews", "Popup Views");
        /*if (getPage().getBrowserWindowWidth() >= 768) {
            menuItems.put("calendar", "Calendar");
        }*/
        //menuItems.put("forms", "Forms");

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

        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        //final StringGenerator sg = new StringGenerator();
        //final MenuItem settingsItem = settings.addItem(sg.nextString(true)
        //        + " " + sg.nextString(true) + sg.nextString(false),
        //        new ThemeResource("../tests-valo/img/profile-pic-300px.jpg"),
        //        null);
        final MenuItem settingsItem = settings.addItem("Mr. Murata",
                new ThemeResource("../tests-valo/img/profile-pic-300px.jpg"),
                null);
        settingsItem.addItem("Edit Profile", null);
        settingsItem.addItem("Preferences", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", null);
        menu.addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        Label label = null;
        int count = -1;
        for (final Entry<String, String> item : menuItems.entrySet()) {
            if (item.getKey().equals("job")) {
                label = new Label("Scheduler", ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("user")) {
                label.setValue(label.getValue()
                        + " <span class=\"valo-menu-badge\">" + count
                        + "</span>");
                count = 0;
                label = new Label("Authorization", ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("kit1")) {
                label.setValue(label.getValue()
                        + " <span class=\"valo-menu-badge\">" + count
                        + "</span>");
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
                    navigator.navigateTo(item.getKey());
                }
            });
            if (count == 1) {
                b.setCaption(b.getCaption()
                        + " <span class=\"valo-menu-badge\">123</span>");
            }
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName("valo-menu-item");
            //b.setIcon(testIcon.get());
            b.setIcon(new Icons(item.getKey()).get());
            menuItemsLayout.addComponent(b);
            count++;
        }
        label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">"
                + count + "</span>");

        return menu;
    }

    private Component createThemeSelect() {
        final NativeSelect ns = new NativeSelect();
        ns.setNullSelectionAllowed(false);
        ns.setId("themeSelect");
        ns.addContainerProperty("caption", String.class, "");
        ns.setItemCaptionPropertyId("caption");
        for (final String identifier : themeVariants.keySet()) {
            ns.addItem(identifier).getItemProperty("caption")
                    .setValue(themeVariants.get(identifier));
        }

        ns.setValue("tests-valo");
        ns.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                setTheme((String) ns.getValue());
            }
        });
        return ns;
    }

    static Handler actionHandler = new Handler() {
        private final Action ACTION_ONE = new Action("Action One");
        private final Action ACTION_TWO = new Action("Action Two");
        private final Action ACTION_THREE = new Action("Action Three");
        private final Action[] ACTIONS = new Action[] { ACTION_ONE, ACTION_TWO,
                ACTION_THREE };

        @Override
        public void handleAction(final Action action, final Object sender,
                final Object target) {
            Notification.show(action.getCaption());
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return ACTIONS;
        }
    };

    public static Handler getActionHandler() {
        return actionHandler;
    }

    public static final String CAPTION_PROPERTY = "caption";
    public static final String DESCRIPTION_PROPERTY = "description";
    public static final String ICON_PROPERTY = "icon";
    public static final String INDEX_PROPERTY = "index";

    @SuppressWarnings("unchecked")
	public
    static IndexedContainer generateContainer(final int size,
            final boolean hierarchical) {
        final TestIcon testIcon = new TestIcon(90);
        final IndexedContainer container = hierarchical ? new HierarchicalContainer()
                : new IndexedContainer();
        final StringGenerator sg = new StringGenerator();
        container.addContainerProperty(CAPTION_PROPERTY, String.class, null);
        container.addContainerProperty(ICON_PROPERTY, Resource.class, null);
        container.addContainerProperty(INDEX_PROPERTY, Integer.class, null);
        container
                .addContainerProperty(DESCRIPTION_PROPERTY, String.class, null);
        for (int i = 1; i < size + 1; i++) {
            final Item item = container.addItem(i);
            item.getItemProperty(CAPTION_PROPERTY).setValue(
                    sg.nextString(true) + " " + sg.nextString(false));
            item.getItemProperty(INDEX_PROPERTY).setValue(i);
            item.getItemProperty(DESCRIPTION_PROPERTY).setValue(
                    sg.nextString(true) + " " + sg.nextString(false) + " "
                            + sg.nextString(false));
            item.getItemProperty(ICON_PROPERTY).setValue(testIcon.get());
        }
        container.getItem(container.getIdByIndex(0))
                .getItemProperty(ICON_PROPERTY).setValue(testIcon.get());

        if (hierarchical) {
            for (int i = 1; i < size + 1; i++) {
                for (int j = 1; j < 5; j++) {
                    final String id = i + " -> " + j;
                    Item child = container.addItem(id);
                    child.getItemProperty(CAPTION_PROPERTY).setValue(
                            sg.nextString(true) + " " + sg.nextString(false));
                    child.getItemProperty(ICON_PROPERTY).setValue(
                            testIcon.get());
                    // ((Hierarchical) container).setChildrenAllowed(id, false);
                    ((Hierarchical) container).setParent(id, i);

                    for (int k = 1; k < 6; k++) {
                        final String id2 = id + " -> " + k;
                        child = container.addItem(id2);
                        child.getItemProperty(CAPTION_PROPERTY).setValue(
                                sg.nextString(true) + " "
                                        + sg.nextString(false));
                        child.getItemProperty(ICON_PROPERTY).setValue(
                                testIcon.get());
                        // ((Hierarchical) container)
                        // .setChildrenAllowed(id, false);
                        ((Hierarchical) container).setParent(id2, id);

                        for (int l = 1; l < 5; l++) {
                            final String id3 = id2 + " -> " + l;
                            child = container.addItem(id3);
                            child.getItemProperty(CAPTION_PROPERTY).setValue(
                                    sg.nextString(true) + " "
                                            + sg.nextString(false));
                            child.getItemProperty(ICON_PROPERTY).setValue(
                                    testIcon.get());
                            // ((Hierarchical) container)
                            // .setChildrenAllowed(id, false);
                            ((Hierarchical) container).setParent(id3, id2);
                        }
                    }
                }
            }
        }
        return container;
    }


}