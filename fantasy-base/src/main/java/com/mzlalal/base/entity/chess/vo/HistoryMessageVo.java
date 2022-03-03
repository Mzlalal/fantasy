package com.mzlalal.base.entity.chess.vo;

import cn.hutool.core.date.DateUtil;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 历史消息VO
 *
 * @author Mzlalal
 * @date 2022/2/19 19:34
 */
@Data
@ApiModel("历史消息VO")
@EqualsAndHashCode(callSuper = true)
public class HistoryMessageVo extends BaseEntity {
    private static final long serialVersionUID = 5048960753867921159L;

    @ApiModelProperty(value = "返回状态")
    private Integer state;

    @ApiModelProperty(value = "返回信息")
    private String msg;

    @ApiModelProperty("时间")
    private final String time = DateUtil.now();

    @ApiModelProperty("发送人")
    private String from;

    @ApiModelProperty("接收人")
    private String to;
}
