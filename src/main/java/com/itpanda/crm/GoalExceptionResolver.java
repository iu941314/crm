package com.itpanda.crm;

import com.alibaba.fastjson.JSON;
import com.itpanda.crm.base.ResultInfo;
import com.itpanda.crm.exceptions.AuthException;
import com.itpanda.crm.exceptions.NoLoginException;
import com.itpanda.crm.exceptions.ParamsException;
//import com.sun.xml.internal.ws.handler.HandlerException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//全局异常处理
@Component
public class GoalExceptionResolver implements HandlerExceptionResolver {
    /**
     * TODO：全局异常
     *      1、自定义异常
     *          -返回VIEW
     *          -返回MODLE
     *              从异常中获取失败信息
     * <p>
     * 2、系统异常
     * -返回VIEW
     * -返回MODLE
     * 返回自定义信息
     * 3、视图和MODEL的判断    是否使用了 @ReponseBody   用了就为MODLE
     *
     * @param httpServletRequest  请求
     * @param httpServletResponse 响应
     * @param handler             处理器
     * @param e                   异常
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {

//        非法请求拦截
        if (e instanceof NoLoginException) {
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        ModelAndView mv=new ModelAndView();
//        mv.setViewName("error");
//        mv.addObject("code",400);
//        mv.addObject("msg","系统异常，请稍后再试...");
//
//        ModelAndView mv = new ModelAndView();

        if (e instanceof NoLoginException) {
            NoLoginException ne =
                    (NoLoginException) e;
            mv.setViewName("no_login");
            mv.addObject("msg", ne.getMsg());

            mv.addObject("ctx", httpServletRequest.getContextPath());
            return mv;
        }


//        //定义默认视图
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("code", 500);
//        modelAndView.addObject("msg", "系统异常，请重试|联系管理员010101");

        //登录拦截

        //判断返回值是视图还是数据
        if (handler instanceof HandlerMethod) {
            //转为HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //通过反射获取处理器是否有@ResponseBody
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //为空为视图 非空为数据
            if (responseBody == null) {
                //判断为 自定义异常   还是系统异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    //自定以异常 从异常中取值
                    mv.addObject("code", p.getCode());
                    mv.addObject("msg", p.getMsg());
                }else if (e instanceof AuthException) {
                    AuthException a = (AuthException) e;
                    //自定以异常 从异常中取值
                    mv.addObject("code", a.getCode());
                    mv.addObject("msg", a.getMsg());
                }
                return mv;

            } else {
                //返回Json数据
//                新建返回体
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试或联系管理员998");
                //判断为 自定义异常   还是系统异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else if (e instanceof AuthException) {
                    AuthException a = (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }
                //设置返回值类型
                httpServletResponse.setContentType("application/json;charset=UTF-8");
//                获取字符输出流
                PrintWriter out = null;
                try {
                    out = httpServletResponse.getWriter();
                    //将返回模型ResultInfo转为json串
                    String result = JSON.toJSONString(resultInfo);
                    out.write(result);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }

                return null;


            }
        }

        return mv;
    }
}
