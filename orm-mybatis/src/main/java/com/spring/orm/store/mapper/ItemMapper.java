package com.spring.orm.store.mapper;

import com.spring.orm.store.model.Item;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ItemMapper {
    @Select("SELECT * FROM item  WHERE item_id = #{itemId}")
    Item getItem(@Param("itemId") Integer itemId);
}
