package com.wangku.miaodan.core.dao;

import com.wangku.miaodan.core.model.PaymentOrder;

public interface PaymentOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentOrder record);

    int insertSelective(PaymentOrder record);

    PaymentOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentOrder record);

    int updateByPrimaryKey(PaymentOrder record);
}