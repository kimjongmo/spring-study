package com.spring.tx.service;

import com.spring.tx.dao.ItemDao;
import com.spring.tx.dao.PaymentInfoDao;
import com.spring.tx.dao.PurchaseOrderDao;
import com.spring.tx.domain.Item;
import com.spring.tx.domain.ItemNotFoundException;
import com.spring.tx.domain.PaymentInfo;
import com.spring.tx.domain.PurchaseOrder;
import org.springframework.transaction.annotation.Transactional;

public class PlaceOrderServiceAnnotImpl implements PlaceOrderService {

	private ItemDao itemDao;
	private PaymentInfoDao paymentInfoDao;
	private PurchaseOrderDao purchaseOrderDao;

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public void setPaymentInfoDao(PaymentInfoDao paymentInformationDao) {
		this.paymentInfoDao = paymentInformationDao;
	}

	public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
		this.purchaseOrderDao = purchaseOrderDao;
	}

	@Override
	@Transactional
	public PurchaseOrderResult order(PurchaseOrderRequest orderRequest)
			throws ItemNotFoundException {
		Item item = itemDao.findById(orderRequest.getItemId());
		if (item == null)
			throw new ItemNotFoundException(orderRequest.getItemId());

		PaymentInfo paymentInfo = new PaymentInfo(item.getPrice());
		paymentInfoDao.insert(paymentInfo);

		PurchaseOrder order = new PurchaseOrder(item.getId(), orderRequest
				.getAddress(), paymentInfo.getId());
		purchaseOrderDao.insert(order);

		return new PurchaseOrderResult(item, paymentInfo, order);
	}

}
