package com.itpanda.crm.aspect;

import com.itpanda.crm.annocation.RequirePermission;
import com.itpanda.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Autowired
    private HttpSession session;

    /**
     * 切面会拦截指定包下的指定注解
     * @param pjp
     * @return
     */
    @Around(value = "@annotation(com.itpanda.crm.annocation.RequirePermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        //获取用户的拥有的权限
        List<String>  permissions = (List<String>) session.getAttribute("permissions");
        //判断拥有的权限
        if(null == permissions || permissions.size() <1){
            throw  new AuthException();
        }

//        获取对应的方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        //获取方法上的租界
        RequirePermission requirePermission = methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);

        //判断对应的状态码
        if(!(permissions.contains(requirePermission.code()))){
            //不包含抛出异常
            throw new AuthException();
        }
        //必须显示调用proceed()
        result = pjp.proceed();
        return result;

    }
}
