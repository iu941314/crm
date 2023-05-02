package com.itpanda.crm.controller;

import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.CustomerLoss;
import com.itpanda.crm.pojo.User;
import com.itpanda.crm.pojo.modle.UserModle;
import com.itpanda.crm.query.CustomerLossQuery;
import com.itpanda.crm.query.UserQuery;
import com.itpanda.crm.services.CustomerLossService;
import com.itpanda.crm.services.UserService;
import com.itpanda.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {
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
    //跳转至添加暂缓|暂缓详情页面
    @RequestMapping("/toCustomerReprPage")
    public String toCustomerReprPage(Integer id, Model model){
        CustomerLoss customerLoss = customerLossService.selectByPrimaryKey(id);
        model.addAttribute("customerLoss",customerLoss);
        return "customerLoss/customer_rep";
    }
    @RequestMapping("/addOrUpdateCustomerReprPage")
    public String addOrUpdateCustomerReprPage(Integer lossId,Model model){
        model.addAttribute("lossId",lossId);
        return "customerLoss/customer_rep_add_update";
    }


    //确认流失用户
    @RequestMapping("updateCustomerLossStateById")
    @ResponseBody
    public ResultInfo updateCustomerLossStateById(Integer id,String lossReason){
        customerLossService.updateCustomerLossStateById(id,lossReason);
        return success("操作成功");
    }


}
