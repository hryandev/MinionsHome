package com.rex.core.components;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class StringToSqlDateConverter implements Converter<String, Date>{

	@Override
	public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		if (targetType != getModelType()) {
            throw new ConversionException("Converter only supports " + getModelType().getName() + " (targetType was " + targetType.getName() + ")");
        }

        if (null == value)
            return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
        Date date = null;
        
        try {
			Date org_date = sdf.parse(value);
			
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			String _value = sdf.format(org_date);
			
			date = sdf.parse(_value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return date;
	}

	@Override
	public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		
		if (targetType != getPresentationType()) {
            throw new ConversionException("Converter only supports " + getPresentationType().getName() + " (targetType was " + targetType.getName() + ")");
        }

        if (null == value)
            return null;
		
        Timestamp ts = new Timestamp(value.getTime());
        
        return ts.toString();
	}

	@Override
	public Class<Date> getModelType() {
		// TODO Auto-generated method stub
		return Date.class;
	}

	@Override
	public Class<String> getPresentationType() {
		// TODO Auto-generated method stub
		return String.class;
	}

}
