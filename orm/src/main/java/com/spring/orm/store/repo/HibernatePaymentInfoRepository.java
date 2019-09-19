package com.spring.orm.store.repo;

import com.spring.orm.store.domain.PaymentInfo;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class HibernatePaymentInfoRepository implements PaymentInfoRepository{

    private SessionFactory sessionFactory;

    public HibernatePaymentInfoRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void save(PaymentInfo paymentInfo) {
        sessionFactory.getCurrentSession().save(paymentInfo);
    }
}
