package com.clara.productapi.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

public class FeignClientAuthInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    @SneakyThrows
    @Override
    public void apply(RequestTemplate template) {
        var currentRequest = getCurrentRequest();
    template.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION) );
    }
    private HttpServletRequest getCurrentRequest() throws ValidationException {
        try {
            return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        }
        catch (Exception e){
            e.printStackTrace();
            throw new ValidationException("The current request could not be processed!!!!");
        }
    }
}
