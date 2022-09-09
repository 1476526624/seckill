package com.xxx.seckill.config;

import com.xxx.seckill.entity.User;
import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.util.CookieUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
/*
      传入的user参数在每个处理方法上都校验会比较麻烦,可以设置User参数校验器
 */
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {


        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;

    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
       return UserContext.getUser();
    }
}
