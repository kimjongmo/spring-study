package com.spring.orm.store.repo;

import com.spring.orm.store.domain.PurchaseOrder;

public interface PurchaseOrderRepository {

	void save(PurchaseOrder order);

}
