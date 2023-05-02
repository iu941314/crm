package com.itpanda.crm.controller;


import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.Module;
import com.itpanda.crm.pojo.SaleChance;
import com.itpanda.crm.pojo.modle.TreeModel;
import com.itpanda.crm.services.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    //跳转至角色授权
    @RequestMapping("/toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest httpServletRequest){

        httpServletRequest.setAttribute("roleId",roleId);

        return "/role/grant";
    }
    //查询所有的module
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    //查询modleList
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }
    @RequestMapping("/index")
    public String moduleIndex(){
        return "/module/module";
    }


    //添加module
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加成功");
    }

    //访问添加窗口
    @RequestMapping("/addModulePage")
    public String addModulePage(Integer grade, Integer parentId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "/module/add";
    }

    //更新module
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("菜单更新成功");
    }


    //访问更新窗口
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id,Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }


    //删除module
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModuleById(id);
        return  success("菜单栏删除成功");
    }

}
