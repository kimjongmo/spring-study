package com.spring.tx.service;


import com.spring.tx.domain.Item;
import com.spring.tx.domain.PaymentInfo;
import com.spring.tx.domain.PurchaseOrder;

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

}
