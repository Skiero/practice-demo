package com.hik.practicedemo.aop;

import com.hik.practicedemo.exception.BusinessException;
import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.constant.CommonConstants;
import com.hik.practicedemo.utils.CasUtil;
import com.hik.practicedemo.utils.IPUtil;
import com.hik.practicedemo.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/28 11:13
 * 访问限流注解拦截器
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class); // 获取注解
            if (null == accessLimit) {
                return true;    // 未添加注解，放行该请求
            }
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            if (needLogin) {
                Optional<Integer> optional = CasUtil.getUserId();
                if (!optional.isPresent()) {
                    // 这里有2种方法实现拦截功能，一种是抛异常，另一种是用 response 写json，然后 return false
                    throw new BusinessException(CommonExceptionEnum.USER_NOT_LOGIN); // 用户未登录抛出异常
                }
            }

            String redisKey = request.getContextPath() + CommonConstants.COLON + request.getServletPath() + CommonConstants.COLON + IPUtil.getHostIP(request);
            String redisValue = RedisUtil.get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)) {
                int count = Integer.parseInt(redisValue);
                if (count < maxCount) {
                    RedisUtil.set(redisKey, String.valueOf(count + 1), accessLimit.seconds(), accessLimit.timeUnit());
                    return true;
                }
                throw new BusinessException(CommonExceptionEnum.TOO_MANY_REQUESTS);
            }
            RedisUtil.set(redisKey, String.valueOf(1), accessLimit.seconds(), accessLimit.timeUnit());  // 新的请求重新计算
            return true;
        }
        return true;
    }
}
