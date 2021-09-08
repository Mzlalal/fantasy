package com.mzlalal.base.advice;

import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * error界面返回JSON
 *
 * @author Mzlalal
 * @date 2021/5/23 11:43
 **/
@Controller
@ResponseStatus(HttpStatus.OK)
public class FantasyErrorController extends AbstractErrorController {

    public FantasyErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @ResponseBody
    @RequestMapping(value = "/error")
    public Result<String> error(HttpServletRequest request) {
        // 获取异常信息
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        // 状态码
        Integer status = (Integer) errorAttributes.getOrDefault("status", 500);
        return GlobalResult.getEnum(status).result();
    }
}
