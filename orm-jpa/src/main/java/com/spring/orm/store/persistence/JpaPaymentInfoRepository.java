package com.spring.orm.store.persistence;

import com.spring.orm.store.domain.PaymentInfo;
import com.spring.orm.store.domain.PaymentInfoRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class JpaPaymentInfoRepository implements PaymentInfoRepository {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Override
	public void save(PaymentInfo paymentInfo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.joinTransaction();
		entityManager.persist(paymentInfo);
	}

}
