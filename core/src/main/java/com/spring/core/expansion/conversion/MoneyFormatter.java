package com.spring.core.expansion.conversion;

import com.spring.core.expansion.Money;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyFormatter implements Formatter<Money> {
    @Override
    public Money parse(String s, Locale locale) throws ParseException {
        Pattern pattern = Pattern.compile("([0-9]+)([A-Z]{3})");
        Matcher matcher = pattern.matcher(s);

        if (!matcher.matches())
            throw new IllegalArgumentException("invalid format");
        int amount = Integer.parseInt(matcher.group(1));
        String currency = matcher.group(2);
        return new Money(amount, currency);
    }

    @Override
    public String print(Money money, Locale locale) {
        return money.toString();
    }
}
