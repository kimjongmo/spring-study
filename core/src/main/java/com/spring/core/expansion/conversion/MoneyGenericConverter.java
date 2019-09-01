package com.spring.core.expansion.conversion;

import com.spring.core.expansion.Money;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyGenericConverter implements GenericConverter {

    private Set<ConvertiblePair> pairs;

    public MoneyGenericConverter() {
        Set<ConvertiblePair> pairs = new HashSet<>();
        pairs.add(new ConvertiblePair(String.class, Money.class));
        this.pairs = pairs;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return pairs;
    }

    @Override
    public Object convert(Object resource, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType() != String.class)
            throw new IllegalArgumentException("invalid source type");
        if (targetType.getType() != Money.class)
            throw new IllegalArgumentException("invalid target type");

        String moneyString = (String) resource;
        Pattern pattern = Pattern.compile("([0-9]+)([A-Z]{3})");
        Matcher matcher = pattern.matcher(moneyString);

        if (!matcher.matches())
            throw new IllegalArgumentException("invalid format");
        int amount = Integer.parseInt(matcher.group(1));
        String currency = matcher.group(2);
        return new Money(amount, currency);
    }
}
