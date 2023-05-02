package com.itpanda.crm.dao;

import com.itpanda.crm.base.BaseMapper;
import com.itpanda.crm.pojo.SaleChance;

import java.util.List;
import java.util.Map;


public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /**
     * 分页查询继承父类
     */

    Integer deleteBatch();

}