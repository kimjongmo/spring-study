package com.spring.orm.store.service;


import com.spring.orm.store.model.ItemNotFoundException;

public interface PlaceOrderService {

	public PurchaseOrderResult order(PurchaseOrderRequest buyRequest) throws ItemNotFoundException;
}
