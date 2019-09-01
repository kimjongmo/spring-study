package com.spring.core.expansion.propertyEditor;

import com.spring.core.expansion.Money;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("([0-9]+)([A-Z]{3})");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches())
            throw new IllegalArgumentException();

        int amount = Integer.parseInt(matcher.group(1));
        String currency = matcher.group(2);
        setValue(new Money(amount, currency));
    }
}
