package com.spring.core.expansion.propertyEditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

    private String datePattern;

    @Override
    public void registerCustomEditors(PropertyEditorRegistry propertyEditorRegistry) {
        CustomDateEditor customDateEditor =
                new CustomDateEditor(new SimpleDateFormat(datePattern));

        propertyEditorRegistry.registerCustomEditor(Date.class, customDateEditor);
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
}
