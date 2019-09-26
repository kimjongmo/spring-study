package com.spring.orm.store.service;

import com.spring.orm.store.domain.*;
import org.springframework.transaction.annotation.Transactional;


public class PlaceOrderServiceImpl implements PlaceOrderService {

	private ItemRepository itemRepository;
	private PaymentInfoRepository paymentInfoRepository;
	private PurchaseOrderRepository purchaseOrderRepository;

	public void setItemRepository(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public void setPaymentInfoRepository(PaymentInfoRepository paymentInformationRepository) {
		this.paymentInfoRepository = paymentInformationRepository;
	}

	public void setPurchaseOrderRepository(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	@Transactional
	@Override
	public PurchaseOrderResult order(PurchaseOrderRequest orderRequest)
			throws ItemNotFoundException {
		Item item = itemRepository.findById(orderRequest.getItemId());
		if (item == null)
			throw new ItemNotFoundException(orderRequest.getItemId());

		PaymentInfo paymentInfo = new PaymentInfo(item.getPrice());
		paymentInfoRepository.save(paymentInfo);

		PurchaseOrder order = new PurchaseOrder(item.getId(), orderRequest
				.getAddress(), paymentInfo.getId());
		purchaseOrderRepository.save(order);
		return new PurchaseOrderResult(item, paymentInfo, order);
	}

}
