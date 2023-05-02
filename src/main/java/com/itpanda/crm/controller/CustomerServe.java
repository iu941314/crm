package com.itpanda.crm.controller;

import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.CustomerLoss;
import com.itpanda.crm.query.CustomerLossQuery;
import com.itpanda.crm.services.CustomerLossService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_serve")
public class CustomerServe extends BaseController {
    @Resource
    private CustomerLossService customerLossService;

    //客户流失列表首页
    @RequestMapping("/index")
    public String toindex(){
        return "/customerLoss/customer_loss";
    }
    //列表展示
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerLossQuery customerLossQuery){
        return customerLossService.queryCustomerByParams(customerLossQuery);
    }


}
