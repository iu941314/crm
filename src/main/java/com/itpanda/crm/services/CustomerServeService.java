package com.itpanda.crm.services;

import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.controller.CustomerServe;
import com.itpanda.crm.dao.CustomerServeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomerServeService extends BaseService<CustomerService,Integer> {
    @Resource
    private CustomerServeMapper customerServeMapper;
}
