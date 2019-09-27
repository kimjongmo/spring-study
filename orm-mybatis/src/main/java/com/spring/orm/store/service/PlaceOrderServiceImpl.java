package com.spring.orm.store.service;

import com.spring.orm.store.mapper.ItemMapper;
import com.spring.orm.store.mapper.PaymentInfoMapper;
import com.spring.orm.store.mapper.PurchaseOrderMapper;
import com.spring.orm.store.model.Item;
import com.spring.orm.store.model.ItemNotFoundException;
import com.spring.orm.store.model.PaymentInfo;
import com.spring.orm.store.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class PlaceOrderServiceImpl implements PlaceOrderService {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private PaymentInfoMapper paymentInfoMapper;
	@Autowired
	private PurchaseOrderMapper purchaseOrderMapper;

	public void setItemMapper(ItemMapper itemMapper) {
		this.itemMapper = itemMapper;
	}

	public void setPaymentInfoMapper(PaymentInfoMapper paymentInfoMapper) {
		this.paymentInfoMapper = paymentInfoMapper;
	}

	public void setPurchaseOrderMapper(PurchaseOrderMapper purchaseOrderMapper) {
		this.purchaseOrderMapper = purchaseOrderMapper;
	}

	@Transactional
	@Override
	public PurchaseOrderResult order(PurchaseOrderRequest orderRequest)
			throws ItemNotFoundException {
		System.out.println("search id = "+orderRequest.getItemId());
		Item item = itemMapper.getItem(orderRequest.getItemId());
		if (item == null)
			throw new ItemNotFoundException(orderRequest.getItemId());

		PaymentInfo paymentInfo = new PaymentInfo(item.getPrice());
		paymentInfoMapper.save(paymentInfo);

		PurchaseOrder order = new PurchaseOrder(item.getItemId(), orderRequest
				.getAddress(), paymentInfo.getId());
		purchaseOrderMapper.save(order);
		return new PurchaseOrderResult(item, paymentInfo, order);
	}

}
