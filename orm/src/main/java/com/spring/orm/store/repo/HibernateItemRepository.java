package com.spring.orm.store.repo;

import com.spring.orm.store.domain.Item;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateItemRepository implements ItemRepository{

    private SessionFactory sessionFactory;

    public HibernateItemRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Item findById(Integer itemId) {
        Item item = (Item) sessionFactory.getCurrentSession().get(Item.class, itemId);
        return item;
    }
}
