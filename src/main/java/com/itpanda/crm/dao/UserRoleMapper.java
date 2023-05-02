package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //通过userId查询所有的用户角色数
    Integer queryUserRolesCount(Integer userId);
    //批量删除用户的权限角色
    Integer deleteUserRolesByUserId(Integer userId);



}