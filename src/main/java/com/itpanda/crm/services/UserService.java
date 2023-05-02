package com.itpanda.crm.services;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itpanda.crm.base.BaseService;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.dao.UserMapper;
import com.itpanda.crm.dao.UserRoleMapper;
import com.itpanda.crm.pojo.User;
import com.itpanda.crm.pojo.UserRole;
import com.itpanda.crm.pojo.modle.UserModle;
import com.itpanda.crm.query.UserQuery;
import com.itpanda.crm.utils.AssertUtil;
import com.itpanda.crm.utils.Md5Util;
import com.itpanda.crm.utils.PhoneUtil;
import com.itpanda.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录
     * @param userName 用户名
     * @param userPwd   密码
     */
    public UserModle userLogin(String userName, String userPwd){
        ResultInfo resultInfo = new ResultInfo();
//        业务逻辑
//        参数非空判断
        checkLoginParams(userName,userPwd);
//        查询数据库已有用户
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(user == null ,"用户不存在！");
        checkUserPwd(userPwd,user.getUserPwd());
        UserModle userModle = buildUseModle(user);
        return userModle;
    }
    @Transactional(propagation =  Propagation.REQUIRED)
    public void updateUserPwd(Integer userId,String old_password,String new_password,String again_password){
        //根据用户id 查询用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == user,"用户不存在");
        //参数校验
        checkUpdatePwdParmas(user,old_password,new_password,again_password);
        //新密码MD5加密，密码替换
        new_password = Md5Util.encode(new_password);
        user.setUserPwd(new_password);
        //执行修改
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"系统错误，更新失败！");

    }

    private void checkUpdatePwdParmas(User user, String old_password, String new_password, String again_password) {
        AssertUtil.isTrue(StringUtils.isBlank(old_password),"用户密码不能为空");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(old_password)),"密码错误,修改失败");
        AssertUtil.isTrue(StringUtils.isBlank(again_password),"确认密码不能为空");
        AssertUtil.isTrue(old_password.equals(new_password),"密码不能与原密码一致");
        AssertUtil.isTrue(!new_password.equals(again_password),"前后密码不一致");

    }

    private UserModle buildUseModle(User user) {
        UserModle userModle = new UserModle();
//        userModle.setUserId(user.getId());
        userModle.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModle.setUserName(user.getUserName());
        userModle.setTrueName(user.getTrueName());
        return  userModle;
    }


    private void checkUserPwd(String userPwd, String pwd) {
        userPwd =Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确");
    }

    private void checkLoginParams(String userName, String userPwd) {
        //用户名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //用户名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }

    //查询所有销售
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }


    //用户分页查询
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        HashMap<String, Object> map = new HashMap<>();
        //分页查询设置
        PageHelper.startPage(userQuery.getPage(), userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        //查询结果设置入map
        map.put("code",0); //layui 要求格式
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;

    }

    /**
     * 用户添加
     * 参数校验：
     *  非空： 用户名  邮箱 手机话
     *  邮箱格式校验
     *  手机号校验
     *
     *  设置默认值
     *  是否有效1
     *  创建时间
     *  更新时间
     *  密码  1234566 ——》MD5加密
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone());
        AssertUtil.isTrue(null != userMapper.queryUserByName(user.getUserName()),"用户名已存在，请重新确认");
        //设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("1234566"));

        //用户名是否存在

        //添加
        AssertUtil.isTrue(userMapper.insertSelective(user)!=1,"添加失败");
        //获取添加成功校色的主键id
        Integer userId = userMapper.queryUserByName(user.getUserName()).getId();
        //绑定用户角色
        bindUserRole(userId,user.getRoleIds());

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void bindUserRole(Integer userId, String roleIds) {
        //通过userId查询用户下的所有角色
        Integer rolesCount = userRoleMapper.queryUserRolesCount(userId);
        //判断是否存在对应的角色，如果存在则删除
        if(rolesCount >0){
            //批量删除用户下的用户校色
            AssertUtil.isTrue(!rolesCount.equals(userRoleMapper.deleteUserRolesByUserId(userId)) ,"用户角色绑定失败");
        }
        //判断用户角色是否存在，如果存在则添加至用户下
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoleList = new ArrayList<>();
            //将roleIds 存入数组中
            String[] roles = roleIds.split(",");
            //遍历角色数组，将角色设置入角色
            for (String role: roles) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(role));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            //执行批量添加
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(),"用户角色绑定失败s……");

        }
    }

    //更新用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
       // 参数校验 更新的时候需要判断用户id 和查询出来的用户id是否一致
        //入参id是否存在
        AssertUtil.isTrue(null == user.getId(),"待更新记录不存在……");
        User temp = userMapper.selectByPrimaryKey(user.getId());
        //数据库是否存在对应数据
        AssertUtil.isTrue(null == temp,"待更新记录不存在……");
        temp = userMapper.queryUserByName(user.getUserName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(user.getId())),"用户已存在！");

        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone());



        //设置默认值
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)!=1,"更新失败");
//        更新用户角色绑定
        bindUserRole(user.getId(),user.getRoleIds());

    }

    private void checkUserParams(String userName, String email, String phone) {
        //参数非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"email不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        //格式校验
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确，请检查");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] userIds) {
       AssertUtil.isTrue(null == userIds || userIds.length <0,"请选择删除记录……");
       AssertUtil.isTrue(deleteBatch(userIds) != userIds.length,"删除失败……");
       //删除绑定的用户角色
        for(Integer userId: userIds){
            //获取对应id的校色记录
            Integer count = userRoleMapper.queryUserRolesCount(userId);
            //删除对应角色下的角色
            AssertUtil.isTrue(!userRoleMapper.deleteUserRolesByUserId(userId).equals(count),"用户删除失败（用户角色删除失败）……");
        }
    }

}
