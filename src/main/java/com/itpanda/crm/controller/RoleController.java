package com.itpanda.crm.controller;


import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.pojo.Role;
import com.itpanda.crm.query.RoleQuery;
import com.itpanda.crm.services.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
@RequestMapping("/role")
@Controller
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;
    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    //角色分页查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryAllRoleByparams(RoleQuery roleQuery){
        return roleService.queryAllRolesByParams(roleQuery);
    }
    //角色管理页面
    @RequestMapping("/index")
    public String RoleManageIndex(){
        return "role/role";
    }


    //返回role编辑|更新页面
    @RequestMapping("/addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer roleId, HttpServletRequest request){
        //判断角色id是否为空  空为添加-不回显数据  非空为更新需要回显数据
        if(null != roleId){
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("roleInfo",role);
        }
        return "role/add_update";
    }

    @RequestMapping("/add")
    @ResponseBody
    //添加或更新roleList
    public ResultInfo addRole(Role role){
            roleService.add(role);
        return success("操作成功!");
    }

    @RequestMapping("/update")
    @ResponseBody
    //添加或更新roleList
    public ResultInfo updateRole(Role role){
            roleService.update(role);
        return success("操作成功!");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deletRole(Integer[] roleIds){
        roleService.deleRole(roleIds);
        return success("删除成功！");
    }

    /**
     *添加权限
     * @param roleId 角色id
     * @param mIds  权限id
     * @return
     */
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return  success("授权成功");
    }
}
