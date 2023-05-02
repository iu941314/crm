package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.Order;

import java.util.Map;

public interface OrderMapper extends BaseMapper<Order,Integer> {


    Order queryLossCustomerOrderByCustomerId(Integer id);

    //通过订单id 查询详情页面所需要的字段
    Map<String, Object> queryCustomerOrderByOrderId(Integer orderId);

}
