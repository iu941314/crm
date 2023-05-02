package com.itpanda.crm.controller;

import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.exceptions.ParamsException;
import com.itpanda.crm.pojo.User;
import com.itpanda.crm.pojo.modle.UserModle;
import com.itpanda.crm.query.UserQuery;
import com.itpanda.crm.services.UserService;
import com.itpanda.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @PostMapping("login")
//    @GetMapping("/user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        UserModle userModle = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModle);
//        try {
////            UserModle userModle = userService.userLogin(userName, userPwd);
////            resultInfo.setResult(userModle);
////        } catch (ParamsException e) {
////            resultInfo.setCode(e.getCode());
////            resultInfo.setMsg(e.getMsg());
////            e.printStackTrace();
////        } catch (Exception e) {
////            resultInfo.setCode(500);
////            resultInfo.setMsg("登录失败");
////        }

        return resultInfo;

    }
    @RequestMapping("toUpdatePwd")
    public String updaPwd() {
        return "user/password";
    }


    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest request,
                                    String old_password, String new_password, String again_password) {
        ResultInfo resultInfo = new ResultInfo();


        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPwd(userId, old_password, new_password, again_password);

//
//        try {
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            userService.updateUserPwd(userId, old_password, new_password, again_password);
//
//        } catch (ParamsException p) {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        } catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("系统错误，修改密码失败");
//            e.printStackTrace();
//        }
        return resultInfo;
    }
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    //返回用户管理视图
    @RequestMapping("/index")
    public String UserManageIndex(){
        return "user/user";
    }
    //用户管理表格数据查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.queryUserByParams(userQuery);
    }
    //添加用户
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功，默认密码1234566");
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户信息更新成功");
    }
    @RequestMapping("/userAddOrUpdate")
    public String userAddOrUpdate(Integer userId,HttpServletRequest httpServletRequest){
//    public String userAddOrUpdate(){
//        判断是更新还是添加  是更新需要回显数据
        if(userId != null){
            //查询该id用户的用户信息，通过域对象回显给前端
            User user = userService.selectByPrimaryKey(userId);
            httpServletRequest.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] userIds){
        userService.deleteUser(userIds);
        return success("删除成功……");
    }


    @RequestMapping("/grant")
    public String roleGrant(Integer userId,HttpServletRequest httpServletRequest){

        return "user/grant";
    }

}
