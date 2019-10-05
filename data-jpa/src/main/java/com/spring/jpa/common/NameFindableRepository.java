package com.spring.jpa.common;

public interface NameFindableRepository<T> {

	T findByName(String name);

}
