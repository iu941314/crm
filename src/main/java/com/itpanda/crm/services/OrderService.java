package com.itpanda.crm.services;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.OrderMapper;
import com.itpanda.crm.dao.UserMapper;
import com.itpanda.crm.pojo.Order;
import com.itpanda.crm.query.OrderQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class OrderService extends BaseService<Order,Integer> {
    @Resource
    private OrderMapper orderMapper;


    //分页查询
    public Map<String,Object> queryCustomerByParams(OrderQuery orderQuery){
        Map<String, Object> map = new HashMap<>();
    //分页
        PageHelper.startPage(orderQuery.getPage(), orderQuery.getLimit());
    PageInfo<Order> pageInfo = new PageInfo<>(orderMapper.selectByParams(orderQuery));
    //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
}

    //通过id查询订单详情页需要的字段
    public Map<String,Object> queryOrderDetailByOrderId(Integer orderId) {
        return orderMapper.queryCustomerOrderByOrderId(orderId);

    }
}
