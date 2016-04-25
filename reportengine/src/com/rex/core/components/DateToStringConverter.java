package com.rex.core.components;

import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class DateToStringConverter implements Converter<Date, String>{

	@Override
	public String convertToModel(Date value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date convertToPresentation(String value, Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<String> getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Date> getPresentationType() {
		// TODO Auto-generated method stub
		return null;
	}



	
}
