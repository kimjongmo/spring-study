package com.spring.orm.store.repo;


import com.spring.orm.store.domain.Item;

public interface ItemRepository {

	Item findById(Integer itemId);

}
