package com.rex.core.components;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class TimestampToDateConverter implements Converter<java.sql.Timestamp, Date>{

	@Override
	public Date convertToModel(Timestamp value, Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		if (targetType != getModelType()) {
            throw new ConversionException("Converter only supports " + getModelType().getName() + " (targetType was " + targetType.getName() + ")");
        }
		
		if (null == value)
			return null;
		
		return new Date(value.getTime());
	}

	@Override
	public Timestamp convertToPresentation(Date value, Class<? extends Timestamp> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		if (targetType != getPresentationType()) {
            throw new ConversionException("Converter only supports " + getPresentationType().getName() + " (targetType was " + targetType.getName() + ")");
        }

        if (null == value)
            return null;
		
		return new java.sql.Timestamp(value.getTime());
	}

	@Override
	public Class<Date> getModelType() {
		// TODO Auto-generated method stub
		return Date.class;
	}

	@Override
	public Class<Timestamp> getPresentationType() {
		// TODO Auto-generated method stub
		return java.sql.Timestamp.class;
	}

}
