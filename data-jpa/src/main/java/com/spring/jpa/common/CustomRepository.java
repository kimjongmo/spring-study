package com.spring.jpa.common;

import java.io.Serializable;


import com.spring.jpa.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepository<T, ID extends Serializable>
		extends JpaRepository<T, ID> {
	public Option<T> getOption(ID id);
}
