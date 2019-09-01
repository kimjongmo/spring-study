package com.spring.core.expansion;

import java.util.Date;

public class StockReaderImpl implements StockReader{
    @Override
    public int getClosePrice(Date data, String code) {
        System.out.println("StockReaderImpl: "+code);
        try{
            Thread.sleep(300);
        }catch (InterruptedException e){

        }
        return 500;
    }
}
