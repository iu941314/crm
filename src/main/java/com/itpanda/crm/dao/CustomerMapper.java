package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.Customer;

import java.util.List;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    //根据用户名查询用户对象
    Customer selectByCustomerName(String name);

    //流失客户查询
    List<Customer> queryLossCustomer();

    //批量修改客户流失状态
    Integer updateCustomerStateByIds(List<Integer> loosCustomerIdList);
}
