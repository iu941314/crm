package com.itpanda.crm.services;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.OrderDetailsMapper;
import com.itpanda.crm.dao.OrderMapper;
import com.itpanda.crm.pojo.Order;
import com.itpanda.crm.pojo.OrderDetails;
import com.itpanda.crm.query.OrderDetailsQuery;
import com.itpanda.crm.query.OrderQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {
    @Resource
    private OrderDetailsMapper orderDetailsMapper;


    //分页查询
    public Map<String,Object> queryCustomerDetailsByParams(OrderDetailsQuery orderDetailsQuery){
        Map<String, Object> map = new HashMap<>();
    //分页
        PageHelper.startPage(orderDetailsQuery.getPage(), orderDetailsQuery.getLimit());
    PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsMapper.selectByParams(orderDetailsQuery));
    //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
}


}
