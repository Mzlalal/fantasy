package com.mzlalal.base.entity.oss.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 待办提醒VO
 *
 * @author Mzlalal
 * @date 2022-03-14 21:45:07
 */
@Data
@Builder
@ApiModel("待办提醒VO")
@NoArgsConstructor
@AllArgsConstructor
public class TodoNotifyVo implements Serializable {
    private static final long serialVersionUID = 8209040326273978051L;

    @ApiModelProperty("备注")
    private String notifyMemo;

    @ApiModelProperty("发送给这些邮箱")
    private String notifyMailSet;

    @NotNull(message = "重复提醒次数不能为空")
    @ApiModelProperty("重复提醒次数")
    private Integer notifyLazyModeTimes;
}
