package com.itpanda.crm.controller;

import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.CustomerReprieve;
import com.itpanda.crm.query.CustomerReprieveQuery;
import com.itpanda.crm.services.CustomerRepeieveService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/customer_rep")
public class CustomerReprieveController extends BaseController {
    @Resource
    private CustomerRepeieveService customerRepeieveService;


    //分页查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> queryCustomerReprieve(CustomerReprieveQuery customerReprieveQuery) {
        return customerRepeieveService.queryCustomerRepeieveByParams(customerReprieveQuery);
    }


    //添加暂缓
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addCustomerReprieve(CustomerReprieve customerReprieve) {
        customerRepeieveService.addCustomerReprieve(customerReprieve);
        return success("添加成功");
    }

    //    更新暂缓项
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateCustomerReprieve(CustomerReprieve customerReprieve) {
        customerRepeieveService.updateCustomerReprieve(customerReprieve);
        return success("添加成功");
    }


    //    删除暂缓项
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCustomerReprieve(CustomerReprieve customerReprieve) {
        customerRepeieveService.deleteCustomerReprieve(customerReprieve);
        return success("删除成功");
    }

}
