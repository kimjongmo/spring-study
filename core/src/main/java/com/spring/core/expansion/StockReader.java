package com.spring.core.expansion;

import java.util.Date;

public interface StockReader {
    public int getClosePrice(Date data, String code);
}
