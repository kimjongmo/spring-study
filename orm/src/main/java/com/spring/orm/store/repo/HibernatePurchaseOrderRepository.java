package com.spring.orm.store.repo;

import com.spring.orm.store.domain.PaymentInfo;
import com.spring.orm.store.domain.PurchaseOrder;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class HibernatePurchaseOrderRepository implements PurchaseOrderRepository {

    private SessionFactory sessionFactory;

    public HibernatePurchaseOrderRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(PurchaseOrder order) {
        sessionFactory.getCurrentSession().save(order);
    }
}
