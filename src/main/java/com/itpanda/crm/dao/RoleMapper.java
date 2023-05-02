package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

     List<Map<String,Object>> queryAllRoles(Integer userId);

    //通过角色名查询角色
    Role selectByRoleName(String roleName);

}