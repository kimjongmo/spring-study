package com.spring.core.expansion.propertyEditor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CustomDateEditor extends PropertyEditorSupport {

    private SimpleDateFormat format;

    public CustomDateEditor(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(format.parse(text));
        } catch (ParseException e) {
            setValue(text);
        }
    }
}
