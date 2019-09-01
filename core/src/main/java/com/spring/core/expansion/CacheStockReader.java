package com.spring.core.expansion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheStockReader implements StockReader {
    private Map<String, Integer> cache = new HashMap<>();
    private StockReader stockReader;

    public CacheStockReader(StockReader stockReader) {
        this.stockReader = stockReader;
    }

    @Override
    public int getClosePrice(Date date, String code) {
        String key = createKey(date, code);
        System.out.println("CacheStockReader: " + key);
        if (cache.containsKey(key))
            return cache.get(key);

        int value = stockReader.getClosePrice(date, code);
        cache.put(key, value);
        return value;
    }

    private String createKey(Date date, String code) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date) + "-" + code;
    }
}
