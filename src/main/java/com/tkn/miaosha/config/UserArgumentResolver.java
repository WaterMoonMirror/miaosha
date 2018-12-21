package com.tkn.miaosha.config;

import com.tkn.miaosha.domain.MiaoshaUser;
import com.tkn.miaosha.redis.RedisService;
import com.tkn.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService userService;


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 判断参数是否为秒杀user的参数
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType== MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String parameter = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
        String cookeValue = getCookeValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
        if (StringUtils.isEmpty(cookeValue) && StringUtils.isEmpty(parameter)) {
            return "login";
        }
        String token = StringUtils.isEmpty(parameter) ? cookeValue : parameter;
        MiaoshaUser user = userService.getByToken(response, token);
        return user;
    }

    private String getCookeValue(HttpServletRequest request, String cookiNameToken) {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals(cookiNameToken)) {
                return c.getValue();
            }
        }
        return null;
    }
}
