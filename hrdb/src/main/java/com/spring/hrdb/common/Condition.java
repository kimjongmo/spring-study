package com.spring.hrdb.common;

import java.util.List;

public interface Condition {
	public String getQuery();
	public List<Object> getParams();
	public boolean isJunction();
}
