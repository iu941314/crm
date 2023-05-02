package com.itpanda.crm.controller;


import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.Customer;
import com.itpanda.crm.pojo.Module;
import com.itpanda.crm.pojo.modle.TreeModel;
import com.itpanda.crm.query.CustomerQuery;
import com.itpanda.crm.services.CustomerService;
import com.itpanda.crm.services.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;
    //分页查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }
//    首页跳转
    @RequestMapping("/index")
    public String index(){
        return "customer/customer";
    }
//    添加客户
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
            customerService.addCustomer(customer);
        return success("用户添加成功");
    }
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
            customerService.updateCustomer(customer);
        return success("用户修改成功");
    }


    //    用户添加页面跳转
    @RequestMapping("/addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id,HttpServletRequest request){
        if(null !=id){
            Customer customer = customerService.selectByPrimaryKey(id);
            request.setAttribute("customer",customer);

        }
        return "customer/add_update";
    }


    //逻辑删除用户
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id){
        customerService.deleteCustomer(id);
        return success("删除成功");
    }

    //    用户添加页面跳转
    @RequestMapping("/orderInfoPage")
    public String orderInfoPage(Integer cid,HttpServletRequest request){
        if(null !=cid){
            Customer customer = customerService.selectByPrimaryKey(cid);
            request.setAttribute("customer",customer);

        }
        return "customer/customer_order";
    }

}
