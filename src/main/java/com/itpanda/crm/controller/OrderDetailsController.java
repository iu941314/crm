package com.itpanda.crm.controller;


import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.query.OrderDetailsQuery;
import com.itpanda.crm.query.OrderQuery;
import com.itpanda.crm.services.OrderDetailsService;
import com.itpanda.crm.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/order_details")
public class OrderDetailsController extends BaseController {

    @Resource
    private OrderDetailsService orderDetailsService;
    //分页查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        return  orderDetailsService.queryCustomerDetailsByParams(orderDetailsQuery);
    }



}
