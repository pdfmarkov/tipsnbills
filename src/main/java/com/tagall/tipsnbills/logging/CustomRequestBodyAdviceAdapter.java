package com.tagall.tipsnbills.logging;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.tagall.tipsnbills.logging.services.LoggingService;
import com.tagall.tipsnbills.module.requested.LoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;


@ControllerAdvice
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    @Autowired
    LoggingService loggingService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        if (httpServletRequest.getRequestURI().equals("/api/auth/signin") ||
                httpServletRequest.getRequestURI().equals("/api/auth/signup")) {
            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUsername(body.toString().substring(body.toString().indexOf("=") + 1, body.toString().indexOf(",")));
            loginRequestDto.setPassword("******"); // It was the fastest solution ;)
            loggingService.logRequest(httpServletRequest, loginRequestDto);
        }
        else loggingService.logRequest(httpServletRequest, body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}