package com.itpanda.crm.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.ModuleMapper;
import com.itpanda.crm.dao.PermissionMapper;
import com.itpanda.crm.dao.RoleMapper;
import com.itpanda.crm.pojo.Permission;
import com.itpanda.crm.pojo.Role;
import com.itpanda.crm.query.RoleQuery;
import com.itpanda.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有角色
     */

    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * role分页查询
     */
    public Map<String, Object> queryAllRolesByParams(RoleQuery roleQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页查询
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getLimit());
        //查询出数据库中的所有角色放置进分页对象中
        PageInfo<Role> pageInfo = new PageInfo<>(roleMapper.selectByParams(roleQuery));
        //设置进返回结果map对象中
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }
//
//    //用户角色添加或更新
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void addOrUpdateRole(Role role) {
//        //参数校验
//        checkParams(role.getRoleName(),role.getRoleRemark());
//        //入库
//        if(null ==role.getId()){
//            //添加
//            AssertUtil.isTrue(roleMapper.insertSelective(role)!=1,"用户角色添加失败");
//        }else{
//            //更新
//            AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) !=1,"用户更新失败");
//        }
//
//
//    }


    //用户角色参数校验
    private void checkParams(String roleName, String roleRemark) {
        AssertUtil.isTrue(StringUtils.isBlank(roleName), "角色名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(roleRemark), "角色备注不能为空");

    }

    //Role删除
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleRole(Integer[] roleIds) {
        AssertUtil.isTrue(null == roleIds || roleIds.length<0, "请选择删除记录");
        AssertUtil.isTrue(deleteBatch(roleIds) != roleIds.length,"删除失败");
    }

    public void add(Role role) {
        //参数校验
        checkParams(role.getRoleName(), role.getRoleRemark());

        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp, "角色已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        //添加
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1, "用户角色添加失败");


    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Role role) {
        //参数校验
        checkParams(role.getRoleName(), role.getRoleRemark());

        Role temp =roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())) ,"该角色已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色记录更新失败!");




        //入库
        role.setUpdateDate(new Date());

        //更新
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "用户更新失败");

    }

    /**
     * 角色授权
     *  如果已有权限 删除所有的权限 添加所有选中权限 无权限 添加所有权限
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        //查询校色下对应的权限记录
        Integer count = permissionMapper.queryAllPermissonByRoleId(roleId);
        //若有删除对应的记录
        if(count >0){
            permissionMapper.deletePermissonByRoleId(roleId);
        }
        //添加最新权限
        if(null != mIds && mIds.length > 0){
            List<Permission> permissionList = new ArrayList<>();

            for (Integer mId: mIds) {
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permissionList.add(permission);
            }
//        入库
            AssertUtil.isTrue(permissionMapper.insertBath(permissionList)!= mIds.length,"添加权限失败");
        }

    }
}

