package com.mzlalal.base.entity.oss.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户信息下拉框查询请求
 *
 * @author Mzlalal
 * @date 2022/3/25 22:29
 **/
@Data
@ApiModel("用户信息下拉框查询请求")
public class UserVueSelectReq implements Serializable {
    private static final long serialVersionUID = -2398907302450016019L;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
}
