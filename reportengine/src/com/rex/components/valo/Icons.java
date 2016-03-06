package com.rex.components.valo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

/**
 *
 * @since
 * @author
 */
public class Icons {

    String key = "";

    public Icons(String key) {
    	 this.key = key;
    }

    public Resource get() {
       return new ThemeResource("../icons/png/" + key + ".png");
    }

}
