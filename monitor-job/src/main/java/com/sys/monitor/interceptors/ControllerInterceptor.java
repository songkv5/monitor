package com.sys.monitor.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.sys.monitor.entity.resp.HttpResponse;
import com.sys.monitor.exception.MonitorException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:29
 */
@Component
@Aspect
@Slf4j
public class ControllerInterceptor {
    @Pointcut("execution(* com.sys.monitor.controller..*.*(..)) && @within(org.springframework.web.bind.annotation.RestController)")
    public void aroundPointCut() {
    }

    @Around("aroundPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String urlPath = request.getRequestURI();
        Object result = null;

        try {
            result = pjp.proceed();
            if (result instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) result;
                Object data = response.getData();
                if (data == null) {
                    response.setData(new JSONObject());
                    response.setMsg("接口请求成功");
                    result = response;
                }
            }
        } catch (Exception e) {
            log.error("接口{}调用失败", e);
            HttpResponse response = new HttpResponse();
            if (e instanceof MonitorException) {
                log.error("接口调用失败, error msg={}", e.getMessage(), e);
                MonitorException exception = (MonitorException) e;
                response.setCode(exception.getCode());
                response.setMsg(exception.getMessage());
            } else {
                response.setCode(500);
                e.printStackTrace();
                log.error("接口调用失败,", e);
                response.setMsg("服务器偷懒了");
            }
            result = response;
        }
        return result;
    }
}
