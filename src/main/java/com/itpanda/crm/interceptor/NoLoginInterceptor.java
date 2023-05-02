package com.itpanda.crm.interceptor;

import com.itpanda.crm.exceptions.NoLoginException;
import com.itpanda.crm.services.UserService;
import com.itpanda.crm.utils.LoginUserUtil;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    /**
     *  拦截用户是否登录
     *  是否有用户id
     *  对象用户id是否用户记录
     *  拦截 重定向至登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //从请求cookie中获取用户id并解析
//        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
//        //判断
//
//        if(userId == 0 || null == userService.selectByPrimaryKey(userId)){
////            抛出未登录异常
//            throw new NoLoginException();
//        }
//
//        return super.preHandle(request,response,handler);



        /**
         *  获取cookie 解析用户id
         *     如果用户id 存在 并且数据库中存在对应记录 请求合法   反之 用户未登录 请求非法
         */
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId==0 || null== userService.selectByPrimaryKey(userId)){
            throw  new NoLoginException();
        }
        return super.preHandle(request, response, handler);
    }
}
