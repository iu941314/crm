package com.itpanda.crm.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;

import com.itpanda.crm.dao.SaleChanceMapper;
import com.itpanda.crm.enums.DevResult;
import com.itpanda.crm.enums.StateStatus;
import com.itpanda.crm.pojo.SaleChance;
import com.itpanda.crm.query.SaleChanceQuery;
import com.itpanda.crm.utils.AssertUtil;
import com.itpanda.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static sun.security.provider.certpath.PKIX.checkParams;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = new HashMap<>();
        //分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance){
        /**
         * 1.参数校验
         *      customerName  客户名非空
         *      linkMan  非空
         *      linkPhone  非空 11位手机号
         * 2. 设置相关参数默认值
         *       state 默认未分配   如果选择分配人  state 为已分配状态
         *       assignTime 默认空   如果选择分配人  分配时间为系统当前时间
         *       devResult  默认未开发  如果选择分配人 devResult 为开发中 0-未开发  1-开发中 2-开发成功 3-开发失败
         *       isValid  默认有效(1-有效  0-无效)
         *       createDate  updateDate:默认系统当前时间
         * 3.执行添加 判断添加结果
         */
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置默认值
        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.DEV_SUCCESS.getStatus());
        //是否添加指派人
        if(StringUtils.isNoneBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(saleChance)<1," 数据添加失败……");

    }

    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号!");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号格式不合法!");
    }


    /**
     * 更新营销机会
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //参数校验
        //1.营销机会Id获取,判断，非空 数据库中存在
        AssertUtil.isTrue(null == saleChance.getId(),"id为空,更新数据不存在……");
        //通过id查询数据库中数据是否存在
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null == temp,"id查询为空，更新数据不存在……");
        //CustomerNam LinkMan LinkPhone 非空校验
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //2.设置默认值
        saleChance.setUpdateDate(new Date());
        //设置指派人
        //修改前是否存在
        if(StringUtils.isBlank(temp.getAssignMan())){// 不存在
            //修改数据是否存在指派人  存在添加相关信息
            if(!StringUtils.isBlank(saleChance.getAssignMan())){
                //指派时间
                saleChance.setAssignTime(new Date());
                //分配状态  任务状体
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());

            }

        }else{//指派人存在
//            判断是否要更新
            if(StringUtils.isBlank(saleChance.getAssignMan())){
//                更新后为空，更新字段
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else {
//                更新后指派人是否一致 不一致则修改，一致则不修改时间即可
                if(temp.getAssignMan() != saleChance.getAssignMan()){
                    saleChance.setAssignTime(new Date());
                }
            }
        }
        //3.执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) !=1,"saleChce更新失败……");


    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids) {
        //判断Id是否为空
        AssertUtil.isTrue(null == ids || ids.length < 0,"请选择删除记录……");
        //执行删除
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!= ids.length,"删除失败……");
    }



    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        SaleChance temp =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==temp,"待更新记录不存在!");
        temp.setDevResult(devResult);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"机会数据状态更新失败!");
    }










}
