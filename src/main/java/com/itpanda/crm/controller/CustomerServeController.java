package com.itpanda.crm.controller;

import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.CustomerServe;
import com.itpanda.crm.pojo.User;
import com.itpanda.crm.query.CustomerServeQuery;
import com.itpanda.crm.services.CustomerServeService;
import org.apache.coyote.RequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("customer_serve")
public class CustomerServeController extends BaseController {
    @Resource
    private CustomerServeService customerServeService;

    //用户创建模块
    @RequestMapping("/index/{type}")
    public String toindex(@PathVariable Integer type){

        if(type==1){
            return "customerServe/customer_serve";
        }else if(type==2){
            return "customerServe/customer_serve_assign";
        }else if(type==3){
            return "customerServe/customer_serve_proce";
        }else if(type==4){
            return "customerServe/customer_serve_feed_back";
        }else if(type==5){
            return "customerServe/customer_serve_archive";
        }else{
            return "";
        }
    }
    //列表展示
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerServeQuery customerServeQuery){

        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }
    @RequestMapping("/addCustomerServePage")
    public String addCustomerServePage(){
        return "customerServe/customer_serve_add";
    }
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addServManage(CustomerServe customerServe){

        customerServeService.addCustomerServe(customerServe);

        return success("添加成功");
    }


    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updatedServManage(CustomerServe customerServe){
        customerServeService.updateCustomerServe(customerServe);
        return success("更新成功");
    }



}
