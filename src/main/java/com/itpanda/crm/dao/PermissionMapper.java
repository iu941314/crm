package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
//    通过用户id查询权限数量
    Integer queryAllPermissonByRoleId (Integer roleId);
    Integer deletePermissonByRoleId (Integer roleId);
    Integer insertBath(List<Permission> permissionList);

    //查询用户拥有的校色queryUserHasRolesPermissions
    List<String> queryUserHasRolesPermissions(Integer userId);


    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    Integer countPermissionsByModuleId(Integer moduleId);

    Integer deletePermissonByModuled(Integer moduleId);
}
