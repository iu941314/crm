package com.itpanda.crm.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.dao.*;
import com.itpanda.crm.pojo.*;
import com.itpanda.crm.pojo.modle.TreeModel;
import com.itpanda.crm.query.CustomerQuery;
import com.itpanda.crm.query.SaleChanceQuery;
import com.itpanda.crm.utils.AssertUtil;
import com.itpanda.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页查询用户
     * @param customerQuery
     * @return 符合符合leyui表格的json格式
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        Map<String, Object> map = new HashMap<>();
        //分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加用户：
     *      s1:参数校验：
     *              客户名称    非空，唯一
     *               法人代表   非空
     *               手机号    非空格式正确

     *      s2:填充默认值
     *          是否有效
     *           创建时间
     *           修改时间
     *           客户编号   唯一  随机生成 （uuid|时间戳|雪花算法）
     *                      KH+时间戳
     *      s3：入库
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
//        参数校验
        cheackCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        Customer tem = customerMapper.selectByCustomerName(customer.getName());
        AssertUtil.isTrue(null != tem," 用户已存在");
//        设置默认值
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setKhno("KH"+System.currentTimeMillis());
//        入库
        AssertUtil.isTrue(customerMapper.insertSelective(customer) <1,"用户添加失败");
    }
    /**
     * 参数校验
     *  客户名称 name   非空 唯一
     *  法人代表 fr     非空
     *  手机号         非空 格式正确
     */
    private void cheackCustomerParams(String name, String fr, String phone) {
//         客户名称 name   非空 唯一
        AssertUtil.isTrue(StringUtils.isBlank(name),"客户姓名不能为空");
//        法人代表 fr     非空
        AssertUtil.isTrue(null == fr,"法人不能为空");
//        手机号         非空  格式正确
        AssertUtil.isTrue(null == phone || phone == "","手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer) {
        //参数校验
        AssertUtil.isTrue(null == customer.getId(),"待更新记录不存在");
        Customer temp =customerMapper.selectByPrimaryKey(customer.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在");
        cheackCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        temp= customerMapper.selectByCustomerName(customer.getName());
        AssertUtil.isTrue( temp!=null && !(temp.getId().equals(customer.getId())),"该客户已存在" );
        //设置默认值
        customer.setUpdateDate(new Date());
        // 入库
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) <1,"用户更新失败");

    }
    //逻辑删除对应客户
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer id){
        //参数校验
        AssertUtil.isTrue(null == id,"删除客户不存在");
        //设置默认值
        Customer temp = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == temp,"删除客户不存在");
        temp.setIsValid(0);
        //更新数据库
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(temp) < 1,"删除用户失败");

    }


    /**
     * 更新客户的流失状态：
     *  1.查询待流失的客户数据
     *  2.将流失的数据批量添加到客户流失表中
     *  3.批量更新客户的流失状态   0=正常客户  1= 流失客户
     *
     */
    public void updateCustomerState(){
        //查询出待流失的客户数据
        List<Customer> loosCustomerList = customerMapper.queryLossCustomer();

        List<Integer> loosCustomerIdList = new ArrayList<>();
        //将查询流失的客户数据批量添加到客户流失表中
//        判断客户数据是否存在， 存在则添加
        if(null != loosCustomerList && loosCustomerList.size() >0){
            //流失客户的列表
            List<CustomerLoss> customerLossList = new ArrayList<>();
//            遍历查询流失数据

            loosCustomerList.forEach(customer -> {
                //定义流失对象
                CustomerLoss customerLoss = new CustomerLoss();

                //赋值
                //创建时间
                customerLoss.setCreateDate(new Date());
                //客户经理
                customerLoss.setCusManager(customer.getCusManager());
                //客户名称
                customerLoss.setCusName(customer.getName());
                //客户编号
                customerLoss.setCusNo(customer.getKhno());
                //是否有效
                customerLoss.setIsValid(1);
                //修改时间
                customerLoss.setUpdateDate(new Date());
                //流失状态  0 暂缓  1确认流失
                customerLoss.setState(0);
                //最后下单时间 --- 在订单表中
                Order order =  orderMapper.queryLossCustomerOrderByCustomerId(customer.getId());
//                判断是否存在 存在则添加
                if(order != null){
                    customerLoss.setLastOrderTime(order.getOrderDate());
                }
                //流失客户对象 设置进集合中
                customerLossList.add(customerLoss);
                //记录转义对象的id
                loosCustomerIdList.add(customer.getId());

            });

            //批量添加流失记录
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLossList) != loosCustomerList.size(),"客户流失数据转义失败");
            //批量更新状态流失对象的状态
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(loosCustomerIdList) != loosCustomerIdList.size(),"客户流失转移失败");
        }
    }





}
