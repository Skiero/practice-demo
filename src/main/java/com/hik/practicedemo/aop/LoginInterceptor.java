package com.hik.practicedemo.aop;

import com.alibaba.fastjson.JSON;
import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.constant.CommonConstants;
import com.hik.practicedemo.model.vo.ResultVO;
import com.hik.practicedemo.utils.CasUtil;
import com.hik.practicedemo.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/28 16:10
 * 验证是否登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否请求方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 判断是否需要登录认证
            Login login = handlerMethod.getMethodAnnotation(Login.class);
            if (null == login) {
                return true;
            }

            Optional<Integer> optional = CasUtil.getUserId();
            // 未登录时可以采用 1.抛出异常  2.response写json返回信息
            if (!optional.isPresent()) {
                ResultVO error = ResultVO.error(CommonExceptionEnum.USER_NOT_LOGIN.getCode(), CommonExceptionEnum.USER_NOT_LOGIN.getMsg());
                // 这里采用response写json返回信息的方式
                HttpUtil.write(JSON.toJSONString(error), response);
                return false;
            }
            // 将用户id存放至 HttpServletRequest 域对象中
            request.setAttribute(StringUtils.isEmpty(login.attributeKey()) ? CommonConstants.LOGIN_TOKEN : login.attributeKey(), optional.get());
            return true;
        }
        return true;
    }
}
