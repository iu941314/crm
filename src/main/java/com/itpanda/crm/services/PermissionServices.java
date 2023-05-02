package com.itpanda.crm.services;

import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.PermissionMapper;
import com.itpanda.crm.pojo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class PermissionServices extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;



    public List<String> queryUserHasRolesPermissions(Integer userId) {
        return permissionMapper.queryUserHasRolesPermissions(userId);
    }
}
