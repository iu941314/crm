package com.itpanda.crm.controller;

import com.itpanda.crm.base.BaseController;
import com.itpanda.crm.pojo.User;
import com.itpanda.crm.services.PermissionServices;
import com.itpanda.crm.services.UserService;
import com.itpanda.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;


@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;

    @Resource
    private PermissionServices permissionServices;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/main")
    public String main(HttpServletRequest request) {
        //登录成功后获取cookie中的用户id，根据用户id查询出用户对象，将用户对象返回前端

        //获取cookie中解码后的id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据id查询用户对象
        User user = (User) userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);

        //用户权限控制：通过 t_permission 中的acl_value来控制，结果保存进session中
        List<String> permissions=permissionServices.queryUserHasRolesPermissions(userId);
        request.getSession().setAttribute("permissions",permissions);
//        System.out.println(permissions);
        return "main";
    }





}


