package com.rex.core.components;

import java.util.ArrayList;
import java.util.List;

import com.rex.backend.entity.FreqTest;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;

public class FreqTable extends CustomField<Object>{
	private static final long serialVersionUID = 605765897038807528L;
	
	Table table = new Table();
    BeanItemContainer<FreqTest> freqContainer = new BeanItemContainer<FreqTest>(FreqTest.class);
	
    public FreqTable() {
        table.setContainerDataSource(freqContainer);
        table.setTableFieldFactory(new TableFieldFactory() {
            @Override
            public Field createField(Container container, Object itemId,
                    Object propertyId, Component uiContext) {
                TextField field = new TextField();
                if ("name".equals(propertyId))
                    field.setWidth("10em");
                else if ("year".equals(propertyId))
                    field.setWidth("4em");
                field.setImmediate(true);
                return field;
            }
        });
        table.setVisibleColumns(new Object[]{"name", "year"});
        table.setEditable(!isReadOnly());
        //setCompositionRoot(table);
    }
    
    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<FreqTest> beans = (List<FreqTest>) value;
            freqContainer.removeAllItems();
            freqContainer.addAll(beans);
            table.setPageLength(beans.size());
        } else
            throw new ConversionException("Invalid type");

        super.setPropertyDataSource(newDataSource);
    }
    
    @Override
    public Object getValue() {
        ArrayList<FreqTest> beans = new ArrayList<FreqTest>(); 
        for (Object itemId: freqContainer.getItemIds())
            beans.add(freqContainer.getItem(itemId).getBean());
        return beans;
    }
    
	@Override
	protected Component initContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getType() {
		// TODO Auto-generated method stub
		return ArrayList.class;
	}

}
