package com.itpanda.crm.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.CustomerLossMapper;
import com.itpanda.crm.dao.CustomerMapper;
import com.itpanda.crm.dao.CustomerReprieveMapper;
import com.itpanda.crm.dao.OrderMapper;
import com.itpanda.crm.pojo.Customer;
import com.itpanda.crm.pojo.CustomerLoss;
import com.itpanda.crm.pojo.CustomerReprieve;
import com.itpanda.crm.pojo.Order;
import com.itpanda.crm.query.CustomerQuery;
import com.itpanda.crm.query.CustomerReprieveQuery;
import com.itpanda.crm.utils.AssertUtil;
import com.itpanda.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomerRepeieveService extends BaseService<CustomerReprieve,Integer> {

    @Resource
    private CustomerReprieveMapper customerReprieveMapper;
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页查询暂缓列表
     * @param customerReprieveQuery
     * @return 符合符合leyui表格的json格式
     */
    public Map<String,Object> queryCustomerRepeieveByParams(CustomerReprieveQuery customerReprieveQuery){
        Map<String, Object> map = new HashMap<>();
        //分页
        PageHelper.startPage(customerReprieveQuery.getPage(), customerReprieveQuery.getLimit());
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieveMapper.selectByParams(customerReprieveQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加暂缓项
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerReprieve(CustomerReprieve customerReprieve) {
        //参数校验
        //流失客户id  非空 存在
        AssertUtil.isTrue(null == customerReprieve.getLossId() ||
                null == customerLossMapper.selectByPrimaryKey(customerReprieve.getLossId()),"添加失败，流失客户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(customerReprieve.getMeasure()),"添加失败，暂缓措施为空");
        //设置默认参数
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        //入库
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve) <1,"添加暂缓项失败");



    }

    /**
     * 更新暂缓项
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)

    public void updateCustomerReprieve(CustomerReprieve customerReprieve) {
        //参数校验
//        主键id 非空存在
        AssertUtil.isTrue(null == customerReprieve.getId() ||
                customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()) ==null,"更新失败，更新项不存在" );
        //流失客户id  非空 存在
        AssertUtil.isTrue(null == customerReprieve.getLossId() ||
                null == customerLossMapper.selectByPrimaryKey(customerReprieve.getLossId()),"添加失败，流失客户不存在");
        //行动项 非空
        AssertUtil.isTrue(StringUtils.isBlank(customerReprieve.getMeasure()),"更新失败，暂缓措施为空");
        //设置默认值
        customerReprieve.setIsValid(1);
        customerReprieve.setUpdateDate(new Date());
        //入库
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) <1,"更新失败");
    }

    /**
     * 逻辑删除
     *
     *
     * @param customerReprieve
     *
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomerReprieve(CustomerReprieve customerReprieve) {
        //参数校验
        AssertUtil.isTrue(null == customerReprieve || null == customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()),"删除失败，删除记录不存在");
        //设置默认值
        customerReprieve.setIsValid(0);
        //逻辑删除(更新)、

        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) <1,"删除失败");
        }


}
