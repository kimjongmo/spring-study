package com.spring.orm.store.mapper;

import com.spring.orm.store.model.PaymentInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface PaymentInfoMapper {
    @Insert("insert into PAYMENT_INFO (PRICE) values (#{price})")
    @Options(keyProperty = "id", useGeneratedKeys = true)
    void save(PaymentInfo paymentInfo);

}
