package com.spring.orm.store.service;


import com.spring.orm.store.model.Item;
import com.spring.orm.store.model.PaymentInfo;
import com.spring.orm.store.model.PurchaseOrder;

public class PurchaseOrderResult {
    private Item item;
    private PaymentInfo paymentInfo;
    private PurchaseOrder order;

    public PurchaseOrderResult(Item item, PaymentInfo paymentInfo,
                               PurchaseOrder order) {
        this.item = item;
        this.paymentInfo = paymentInfo;
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "PurchaseOrderResult{" +
                "item=" + item +
                ", paymentInfo=" + paymentInfo +
                ", order=" + order +
                '}';
    }
}
