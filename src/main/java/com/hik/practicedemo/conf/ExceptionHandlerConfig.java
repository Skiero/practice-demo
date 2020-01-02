package com.hik.practicedemo.conf;

import com.hik.practicedemo.exception.BusinessException;
import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.vo.ResultVO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * Created by wangJinChang on 2019/12/25 20:51
 * 全局异常处理类
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerConfig {

    @ExceptionHandler(value = Exception.class)
    public ResultVO handleException(Exception e) {
        e.printStackTrace();
        return ResultVO.error(CommonExceptionEnum.OTHER_ERROR.getCode(), CommonExceptionEnum.OTHER_ERROR.getMsg());
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResultVO handleBusinessException(BusinessException e) {
        e.printStackTrace();
        return ResultVO.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        String defaultMessage = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return ResultVO.error(defaultMessage);
    }
}
