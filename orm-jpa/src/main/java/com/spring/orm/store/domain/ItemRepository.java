package com.spring.orm.store.domain;


public interface ItemRepository {

	Item findById(Integer itemId);

}
