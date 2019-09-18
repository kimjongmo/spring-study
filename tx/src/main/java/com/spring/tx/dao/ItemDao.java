package com.spring.tx.dao;


import com.spring.tx.domain.Item;

public interface ItemDao {

	Item findById(Integer itemId);

}
