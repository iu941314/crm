package com.itpanda.crm.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.dao.OrderMapper;
import com.itpanda.crm.pojo.Customer;
import com.itpanda.crm.pojo.Order;
import com.itpanda.crm.pojo.SaleChance;
import com.itpanda.crm.query.CustomerQuery;
import com.itpanda.crm.query.OrderQuery;
import com.itpanda.crm.services.CustomerService;
import com.itpanda.crm.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;
    //分页查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(OrderQuery orderQuery) {
        return  orderService.queryCustomerByParams(orderQuery);
    }

    @RequestMapping("/orderDetailPage")
    public String orderDetailPage(Integer orderId, Model model){
            model.addAttribute("order",orderService.queryOrderDetailByOrderId(orderId));
        System.out.println(orderService.queryOrderDetailByOrderId(orderId));

        return "customer/customer_order_detail";
    }

}
