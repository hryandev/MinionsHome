/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.rex.components.valo;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.rex.core.views.TaskView;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @since
 * @author Vaadin Ltd
 */
public class ValoMenuLayout extends HorizontalLayout {
	
	VerticalLayout blank = new VerticalLayout();

    CssLayout contentArea = new CssLayout();

    CssLayout menuArea = new CssLayout();
    
    public static LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();
    public static CssLayout menu = new CssLayout();
    public static CssLayout menuItemsLayout = new CssLayout();
    public static TaskView taskView = new TaskView();
    public static int taskCount = 0;

    public ValoMenuLayout() {
        setSizeFull();

        menuArea.setPrimaryStyleName("valo-menu");
        
        blank.setSizeFull();

        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();

        addComponents(menuArea, contentArea);
        setExpandRatio(menuArea, 1);
        setExpandRatio(contentArea, 8);
    }

    public ComponentContainer getContentContainer() {
        return contentArea;
    }
    
    public ComponentContainer getBlankContentContainer() {
        return blank;
    }

    public void addMenu(Component menu) {
        menu.addStyleName("valo-menu-part");
        menuArea.addComponent(menu);
    }
    
    public void removeMenu() {
        menuArea.removeAllComponents();
    }
    
    public CssLayout buildMenu(){

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
                	
                	/*if(item.getKey().equals("task")){
                		Page.getCurrent().reload();
                	}*/
                	
                	getUI().getNavigator().navigateTo(item.getKey());
                }
            });
            if (count == 1) {
                b.setCaption(b.getCaption()
                        + " <span class=\"valo-menu-badge\">"+taskCount+"</span>");
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

}
