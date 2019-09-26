package com.spring.orm.store.persistence;

import com.spring.orm.store.domain.PurchaseOrder;
import com.spring.orm.store.domain.PurchaseOrderRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaPurchaseOrderRepository implements PurchaseOrderRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void save(PurchaseOrder order) {
		entityManager.persist(order);
	}

}
