package com.spring.orm;

import com.spring.orm.store.service.PlaceOrderServiceImpl;
import com.spring.orm.store.service.PurchaseOrderRequest;
import com.spring.orm.store.service.PurchaseOrderResult;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        GenericXmlApplicationContext context =
                new GenericXmlApplicationContext("dataSource.xml");
        PlaceOrderServiceImpl placeOrderService
                = context.getBean("placeOrderService", PlaceOrderServiceImpl.class);

        PurchaseOrderRequest request = new PurchaseOrderRequest();
        request.setItemId(1);
        request.setAddress("별내동");
        PurchaseOrderResult result = placeOrderService.order(request);
        System.out.println(result);

        context.close();
    }
}
