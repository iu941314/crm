package com.itpanda.crm.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.CustomerMapper;
import com.itpanda.crm.dao.CustomerServeMapper;
import com.itpanda.crm.dao.UserMapper;
import com.itpanda.crm.pojo.CustomerReprieve;
import com.itpanda.crm.pojo.CustomerServe;
import com.itpanda.crm.pojo.CustomerServeStatus;
import com.itpanda.crm.query.CustomerServeQuery;
import com.itpanda.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServeService extends BaseService<CustomerServe, Integer> {
    @Resource
    private CustomerServeMapper customerServeMapper;
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页查询
     *
     * @param customerServeQuery
     * @return
     */
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        HashMap<String, Object> map = new HashMap<>();
        PageHelper.startPage(customerServeQuery.getPage(), customerServeQuery.getLimit());
        PageInfo<CustomerServe> pageInfo = new PageInfo<>(customerServeMapper.selectByParams(customerServeQuery));
        //设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加
     *
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerServe(CustomerServe customerServe) {

        /** 服务添加操作
         * 1.参数校验
         * 客户名 非空
         * 客户类型 非空
         * 2.添加默认值
         * state 设置状态值
         * isValid createDate updateDate
         * 3.执行添加 判断结果
         */
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()), "请指定客户!");
        AssertUtil.isTrue(null == customerMapper.queryCustomerByName(customerServe.getCustomer()), " 当前客户暂不存在 !");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()), "请指定服务类型!");
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());
        customerServe.setState(CustomerServeStatus.CREATED.getState());
        AssertUtil.isTrue(insertSelective(customerServe) < 1, "服务记录 添加失败 !");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerServe(CustomerServe customerServe) {

        CustomerServe temp = selectByPrimaryKey(customerServe.getId());
        AssertUtil.isTrue(null == temp, "待处理的服务记录不存在!");
        if (customerServe.getState().equals(CustomerServeStatus.ASSIGNED.getState())) {
// 服务分配
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getAssigner())
                    || (null == userMapper.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner()))), "待分配用户不存在");
            customerServe.setAssignTime(new Date());
            customerServe.setUpdateDate(new Date());
            AssertUtil.isTrue(updateByPrimaryKeySelective(customerServe) < 1, "服务分配失败 !");
        }
        if (customerServe.getState().equals(CustomerServeStatus.PROCED.getState())) {
// 服务处理
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "请指定处理内容!");
            customerServe.setServiceProceTime(new Date());
            customerServe.setUpdateDate(new Date());
            AssertUtil.isTrue(updateByPrimaryKeySelective(customerServe) < 1, "服务处理失败 !");
        } if (customerServe.getState().equals(CustomerServeStatus.FEED_BACK.getState())) {
// 服务处理
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "请指定反馈内容!");
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()), "请指定反馈满意度 !");
            customerServe.setUpdateDate(new Date());
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
            AssertUtil.isTrue(updateByPrimaryKeySelective(customerServe) < 1, "服务反馈失败 !");
        }


    }
}

