package com.spring.mvc2.interceptor;

import com.spring.mvc2.model.MemberRegistRequest;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;


public class CommonInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("요청 경로 : "+request.getRequestURI());
        return super.preHandle(request, response, handler);
    }
}
