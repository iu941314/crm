package com.itpanda.crm.controller;

import com.itpanda.crm.annocation.RequirePermission;
import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.enums.StateStatus;
import com.itpanda.crm.pojo.SaleChance;
import com.itpanda.crm.query.SaleChanceQuery;
import com.itpanda.crm.services.SaleChanceService;
import com.itpanda.crm.services.UserService;
import com.itpanda.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private SaleChanceService saleChanceService;


    //分页查询saleChance
    @RequirePermission(code="101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceParams(SaleChanceQuery saleChanceQuery,
                                                    Integer flag,HttpServletRequest request){

        if(flag != null && flag ==1){
            saleChanceQuery.setState(StateStatus.STATED.getType());

            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    //saleChance添加
    @RequirePermission(code = "101002")
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo savaSaleChange(HttpServletRequest request,SaleChance saleChance){
        saleChance.setCreateMan(userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request)).getTrueName());
        saleChanceService.saveSaleChance(saleChance);
        return success("数据添加成功");
    }


    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer saleChanceId,HttpServletRequest request){   //Integer id,Model model
        if(saleChanceId !=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }
    //更新|修改
    @RequirePermission(code = "101004")
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance( SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("机会数据更新成功");
    }
//删除
    @RequirePermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        System.out.println(ids);
        for (int i:ids) {
            System.out.println(i);
        }
        saleChanceService.deleteSaleChance(ids);
        return success("机会数据删除成功!……");
    }


    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }


}
