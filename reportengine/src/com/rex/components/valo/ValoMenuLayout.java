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
	
    protected CssLayout contentArea = new CssLayout();

    protected CssLayout menuArea = new CssLayout();

    public ValoMenuLayout() {
        setSizeFull();

        menuArea.setPrimaryStyleName("valo-menu");

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

    public void addMenu(Component menu) {
        menu.addStyleName("valo-menu-part");
        menuArea.addComponent(menu);
    }

}
