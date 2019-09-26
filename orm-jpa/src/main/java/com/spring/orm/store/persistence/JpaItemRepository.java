package com.spring.orm.store.persistence;

import com.spring.orm.store.domain.Item;
import com.spring.orm.store.domain.ItemRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class JpaItemRepository implements ItemRepository {

	private EntityManagerFactory entityManagerFactory;

	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.entityManagerFactory = emf;
	}

	@Override
	public Item findById(Integer itemId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.joinTransaction();
		return entityManager.find(Item.class, itemId);
	}

}
