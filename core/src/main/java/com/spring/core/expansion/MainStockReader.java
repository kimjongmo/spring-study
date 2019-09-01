package com.spring.core.expansion;

import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Date;

public class MainStockReader {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:/stockreader.xml");
        StockReader stockReader = ctx.getBean("stockReader", StockReader.class);
        System.out.println("stockReader = "+stockReader.getClass().getName());
        Date date = new Date();
        printClosePrice(stockReader, date, "0000");
        printClosePrice(stockReader, date, "0000");

        ctx.close();
    }

    private static void printClosePrice(StockReader stockReader, Date date, String code) {
        long before = System.currentTimeMillis();
        int stockPrice = stockReader.getClosePrice(date, code);
        long after = System.currentTimeMillis();
        System.out.println("읽어온 값 = " + stockPrice + ", 실행 시간 = " + (after - before));
    }
}
