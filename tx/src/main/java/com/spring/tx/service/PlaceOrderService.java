package com.spring.tx.service;


import com.spring.tx.domain.ItemNotFoundException;

public interface PlaceOrderService {

	public PurchaseOrderResult order(PurchaseOrderRequest buyRequest) throws ItemNotFoundException;
}
