package com.tkn.miaosha.exception;

import com.alibaba.druid.sql.visitor.functions.Bin;
import com.tkn.miaosha.result.CodeMsg;
import com.tkn.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value=Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException g= (GlobalException) e;
            return Result.error(g.getCm());
        }else if (e instanceof BindException){
            BindException be= (BindException) e;
            List<ObjectError> allErrors = be.getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String defaultMessage = objectError.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(defaultMessage));
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
