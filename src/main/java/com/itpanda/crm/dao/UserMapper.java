package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMapper<User,Integer>{
    User queryUserByName(String userName);

    List<Map<String,Object>> queryAllSales();

}