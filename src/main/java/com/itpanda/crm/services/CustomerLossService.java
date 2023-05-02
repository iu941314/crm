package com.itpanda.crm.services;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.CustomerLossMapper;
import com.itpanda.crm.pojo.CustomerLoss;
import com.itpanda.crm.pojo.CustomerReprieve;
import com.itpanda.crm.pojo.SaleChance;
import com.itpanda.crm.query.CustomerLossQuery;
import com.itpanda.crm.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 列表分页查询
     * @return
     */
    public Map<String, Object> queryCustomerByParams(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map = new HashMap<>();
        //分页
        PageHelper.startPage(customerLossQuery.getPage(), customerLossQuery.getLimit());
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossMapper.selectByParams(customerLossQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }



    /**
     * 确认流失客户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        //参数校验
        //流失 id  非空 存在
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(id);

        AssertUtil.isTrue(null == id || null== customerLoss,"流失失败,流失客户不存在");
        //流失原因非空
        AssertUtil.isTrue(null == lossReason,"流失原因不能为空");
        //设置默认值
        customerLoss.setState(1);
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());
        //入库
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss) <1 ,"流失失败");
    }



}
